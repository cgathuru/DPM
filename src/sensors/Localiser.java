package sensors;

import navigation.Obstacle;
import communication.Decoder;
import communication.StartCorner;

import robot.Odometer;
import robot.TwoWheeledRobot;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import main.Constants;

public class Localiser {
	private LightSampler leftLight, rightLight;
	private UltrasonicSensor usLeft;
	private TwoWheeledRobot robot;
	private Odometer odometer;
	private StartCorner startCorner;
	private double fieldSize;
	
	/**
	 * Initialises all the variables used by the localisation routine
	 * @param robot The {@link TwoWheeledRobot} that controls the robots movements
	 * @param usLeft The left Ultrasonic sensor
	 * @param leftLight The left {@code LightSampler}
	 * @param rightLight The right {@code LightSampler}
	 */
	public Localiser(TwoWheeledRobot robot, UltrasonicSensor usLeft, LightSampler leftLight, LightSampler rightLight){
		this.robot = robot;
		this.odometer = robot.getOdometer();
		this.usLeft = usLeft;
		this.leftLight = leftLight;
		this.rightLight = rightLight;
		this.startCorner = Decoder.startCorner;
		fieldSize = Constants.PLAYING_FEILD.length * Constants.TILE_DISTANCE;
		
	}
	
	/**
	 * Starts the localisation of the robot from its target square
	 */
	public void dolocalise(){
		//new LCDInfo(odo);
				usLeft.getDistance();
				//if facing towards the wall initially rotate until facing no wall
				correctStartingDirection();
				
				//if facing away from the wall
				if(Obstacle.getLeftUsDistance()  > Constants.WALL_DIST){
					boolean loacalized = false;
					while(!loacalized){
						robot.setRotationSpeed(Constants.ROTATE_SPEED);
						//rotate clockwise till you see a wall
						if(Obstacle.getLeftUsDistance()  < Constants.WALL_DIST){
							//patBot.turnToImmediate(-55);
							robot.stopMotors();
							doAlignmentRoutine();
							loacalized = true;
							
						}//end if distance < 60
						
					}//end while localised
				}//end if facing away from wall
	}//end do localise

	public void doAlignmentRoutine() {
		robot.turnToImmediate(-25);
		robot.moveForward(); //move forawrd until you hit a line
		doFirstAlignment();
		robot.turnTo(-90);
		robot.moveForward();
		doSecondAlignment();
	}

	/**
	 * Does the second alignment for the robots localisation which sets x or y,
	 * depending on the localisation {@link StartCorner}
	 */
	public void doSecondAlignment() {
		boolean there = false;
		while(!there){
			if(leftLight.darkLineCheck()){
				robot.stopMotors();
				boolean last = false;
				Motor.B.setSpeed(-Constants.ROTATE_SPEED);
				Motor.B.backward();
				while(!last){
					if(rightLight.isDarkLine()){
						Motor.B.stop();
						last = true;
						break;
					}
				}
				correctAndBypassObstacle();
				
				//odo.setY(0);
				there = true;
				
				//dometer.setX(0);
				
				break;
				
			}//end leftLight isDark line
		}//end while there
	}

	/**
	 * Sets x or y, depending on the localisation {@link StartCorner}
	 * and travels out of range of the obstacle to avoid calling
	 * obstacle avoidance which would slow the robot down.
	 */
	public void correctAndBypassObstacle() {
		double odoY = odometer.getY();
		double odoX = odometer.getX();
		double yValue = (int)(odoY/30) *Constants.TILE_DISTANCE;
		double xValue = (int)(odoX/30) *Constants.TILE_DISTANCE;
		switch(startCorner){
		case BOTTOM_LEFT:
			odometer.setY(yValue);
			odometer.setTheta(0);
			robot.turnTo(0);
			robot.travelTo(60, 0);
			robot.travelTo(60, 30);
			break;
		case BOTTOM_RIGHT:
			odometer.setX(fieldSize - (xValue + 2*Constants.TILE_DISTANCE));
			odometer.setTheta(270);
			robot.travelTo(240, 0);
			robot.travelTo(240, 30);
			break;
		case NULL: //for null case assume bottom right
			odometer.setY(yValue);
			odometer.setTheta(0);
			robot.turnTo(0);
			robot.travelTo(60, 0);
			robot.travelTo(60, 30);
			break;
		case TOP_LEFT:
			odometer.setX(xValue);
			odometer.setTheta(90);
			robot.travelTo(60, 300);
			robot.travelTo(60, 270);
			break;
		case TOP_RIGHT:
			odometer.setY(fieldSize - (yValue + 2*Constants.TILE_DISTANCE));
			odometer.setTheta(180);
			robot.travelTo(240, 300);
			robot.travelTo(240, 270);
			break;
		default:
			break;
		
		}
	}

	/**
	 * Does the first alignment for the robots localisation which sets x or y,
	 * depending on the localisation {@link StartCorner}
	 */
	public void doFirstAlignment() {
		boolean light = false;
		boolean light2 = false;
		while(!light){
			if(rightLight.darkLineCheck()){
				Motor.B.stop(true);
				Motor.A.stop();
				Motor.A.setSpeed(-Constants.ROTATE_SPEED);
				Motor.A.backward();
				light = true;
				//Sound.beepSequenceUp();
				
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!light2){
		if(leftLight.darkLineCheck()){
			Sound.beepSequenceUp();
			robot.stopMotors();
			Motor.A.stop();
			Sound.buzz();
			odometer.setTheta(0);
			double odoX = odometer.getX();
			double odoY = odometer.getY();
			int xValue = (int)(odoX/30) *30;
			int yValue = (int)(odoY/30) *30;
			switch(startCorner){	
			case BOTTOM_LEFT:			
				odometer.setX(xValue);
				break;
			case BOTTOM_RIGHT:
				odometer.setY(yValue);
				break;
			case NULL: //for the null case assume bottom left
				odometer.setX(xValue);
				break;
			case TOP_LEFT:
				odometer.setY(fieldSize - (yValue + 2*Constants.TILE_DISTANCE));
				break;
			case TOP_RIGHT:
				odometer.setX(fieldSize - (xValue + 2*Constants.TILE_DISTANCE));
				break;
			}
			
			light2 = true;
			light = true;
			break;
		}
	}
		
	}

	/**
	 * If facing towards the wall initially, the robot will rotate until facing
	 * no wall so that the Ultrasonic sensor can detect the right edge
	 * required for successful localisation
	 */
	public void correctStartingDirection() {
		if(Obstacle.getLeftUsDistance() < Constants.WALL_DIST){
			boolean wrongDirection = true;
			robot.setRotationSpeed(Constants.ROTATE_SPEED);
			while(wrongDirection){
				if(usLeft.getDistance() > Constants.WALL_DIST){
					//robot.stopMotors();
					wrongDirection = false;
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}//end localiser
