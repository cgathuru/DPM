package navigation;

import lejos.nxt.Motor;
import main.Constants;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightSampler;

import communication.Decoder;

/**
 * Implements the offensive strategy of the robot
 * @author charles
 *
 */
public class Offence extends Navigation implements Strategy{
	private OdometryCorrection odoCorrection;
	private TwoWheeledRobot robot;
	private Decoder decoder;

	/**
	 * Initializes all the parameters needed {@link Navigation}
	 * @param robot A {@link TwoWheeledRobot}
	 * @param obstacle An {@link Obstacle} responsible for obstacle avoidance
	 * @param odoCorrection The {@link OdometryCorrection}
	 * @param decoder 
	 */
	public Offence(TwoWheeledRobot robot, ZachObstacle obstacle, OdometryCorrection odoCorrection, Decoder decoder) {
		super(robot, obstacle, odoCorrection);
		this.robot = robot;
		this.odoCorrection = odoCorrection;
		this.decoder = decoder;
	}
	
	/**
	 * The default method called to start the defensive strategy.
	 */
	@Override
	public void start() {
		odoCorrection.startCorrectionTimer();
		collectBalls();
		//super.startCorrectionTimer();
		travelToShootingLocation();
		shoot();
		
	}
	
	/**
	 * Navigates to the ball dispensers location and collects the balls
	 */
	public void collectBalls(){
		int xTarget = decoder.dispenserX;
		int yTarget = decoder.dispenserY;
		travelNearCollectionSite(xTarget, yTarget);
		turnOffObstacleAvoidance();
		//super.stopCorrectionTimer();
		robot.turnToFace(xTarget, yTarget);
		//localizeHere();
		//robot.turnToImmediate(-15);
		robot.moveForwardBy(22);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		//collect 4 more balls
		//for( int i =1; i< 5; i++){
			//collectAnotherBall(i);
		//}

		//robot.moveForwardBy(-25);
		robot.moveForwardBy(-20, true);
		
		localizeAtCollection(xTarget, yTarget);
		turnOnObstacleAvoidance();
		//robot.turnToImmediate(15);
		//super.stopCorrectionTimer();
	}

	private void localizeAtCollection(int xTarget, int yTarget) {
		//odoCorrection.stopCorrectionTimer(false);
		LightSampler left = odoCorrection.getLeftLightSampler();
		LightSampler right = odoCorrection.getRightLightsmapler();
		Odometer odometer = robot.getOdometer();
		boolean localized = false;
		while(!localized){
			if(left.isDarkLine()){
				robot.stopMotors();
				//if x ordinate is negative and y is positive
				if(xTarget < 0){
					odometer.setX(0);
					odometer.setY(yTarget);
				}
				//if x ordinate is positive and y is negative
				else if (xTarget > 0 && yTarget < 0){
					odometer.setX(xTarget);
					odometer.setY(0);
				}
				//if x ordinate is positive between 0 and 300 and y ordinate is positive
				else if (yTarget >0 && yTarget < 300 && xTarget > 0){
					odometer.setX(xTarget - Constants.TILE_DISTANCE);
					odometer.setY(yTarget);
				}
				//if y ordinate is yTarget > 300
				else{
					odometer.setX(xTarget);
					odometer.setY(yTarget- Constants.TILE_DISTANCE);
				}
				localized =true;
			}
		}
		
		
		
		//robot.turnToImmediate(10);
		//UltrasonicSensor leftUs = Obstacle.getLeftUs();
		//new Localiser(robot, leftUs, left, right, decoder).doAlignmentRoutine();
		//left.getLightValue();
		//new LightLocalizer(robot, left).doLocalization();
		//new ReLocaliser(robot, left, right).reLocalize();
		//robot.turnToImmediate(90);
		//super.moveForwardBy(35);
	}
	
	/**
	 * Collects another ball from the ball dispenser
	 */
	public void collectAnotherBall(int i){
		super.moveForwardBy(-3);
		super.moveForwardBy(4.5);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Moves the robot to a target destination in line with the ball dispenser so that the robot can approach
	 * the dispenser head on, instead of at an angle.
	 * @param xTarget The x ordinate of the ball dispensers location
	 * @param yTarget The y ordinate of the ball dispenser location
	 */
	public void travelNearCollectionSite(int xTarget, int yTarget){
		//robot.getLeftMotor().setSpeed(Constants.FORWARD_SPEED);
		//robot.getRightMotor().setSpeed(Constants.FORWARD_SPEED);
		//robot.getLeftMotor().forward();
		//robot.getRightMotor().forward();
		robot.turnToFace(xTarget, yTarget);
		
		//if x ordinate is negative and y is positive
		if(xTarget < 0){
			super.travelTo(0, yTarget);
			super.travelTo(0, yTarget);
		}
		//if x ordinate is positive and y is negative
		else if (xTarget > 0 && yTarget < 0){
			super.travelTo(xTarget, 0);
			super.travelTo(xTarget, 0);
		}
		//if x ordinate is positive between 0 and 300 and y ordinate is positive
		else if (yTarget >0 && yTarget < 300 && xTarget > 0){
			super.travelTo(xTarget - Constants.TILE_DISTANCE_TRUNCATED, yTarget);
			super.travelTo(xTarget - Constants.TILE_DISTANCE_TRUNCATED, yTarget);
		}
		//if y ordinate is yTarget > 300
		else{
			super.travelTo(xTarget, yTarget- Constants.TILE_DISTANCE_TRUNCATED);
			super.travelTo(xTarget, yTarget- Constants.TILE_DISTANCE_TRUNCATED);
		}
	}

	/**
	 * Turns off the robots obstacle avoidance so that the robot can
	 * hit the ball dispenser touch sensors
	 */
	public void turnOffObstalceAvoidance(){
		super.turnOffObstacleAvoidance();
	}

	public void turnOnObstacleAvoidacne(){
		super.turnOnObstacleAvoidance();
	}
	/**
	 * Moves the robot to its optimal shooting location
	 */
	public void travelToShootingLocation(){
		odoCorrection.startCorrectionTimer();
		int xTarget = decoder.shootX;
		int yTarget = decoder.shootY;
		travelNearShootingLocation();
		//localizeHere();
		super.travelTo(xTarget, yTarget);
	}
	
	public void travelNearShootingLocation(){
		int xTarget = decoder.shootX;
		int yTarget = decoder.shootY;
		int tilesX = (xTarget + Constants.TILE_DISTANCE_TRUNCATED/2 +2);
		super.travelTo(tilesX, yTarget);

	}
	
	
	/**
	 * Shoots the balls at the goal with the {@link Launcher}
	 */
	public void shoot(){
		robot.turnTo(0);
		Launcher.drive(Motor.A, Motor.B, Motor.C);
	}
}
