package navigation;

import lejos.nxt.Motor;
import main.Constants;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.OdometryCorrection.SensorSide;
import robot.TwoWheeledRobot;
import sensors.LightLocalizer;
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

	/**
	 * Initializes all the parameters needed {@link Navigation}
	 * @param robot A {@link TwoWheeledRobot}
	 * @param obstacle An {@link Obstacle} responsible for obstacle avoidance
	 * @param odoCorrection The {@link OdometryCorrection}
	 */
	public Offence(TwoWheeledRobot robot, Obstacle obstacle, OdometryCorrection odoCorrection) {
		super(robot, obstacle, odoCorrection);
		this.robot = robot;
		this.odoCorrection = odoCorrection;
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
		int xTarget = Decoder.dispenserX;
		int yTarget = Decoder.dispenserY;
		travelNearCollectionSite(xTarget, yTarget);
		turnOffObstacleAvoidance();
		//super.stopCorrectionTimer();
		//localizeHere();
		robot.turnToFace(xTarget, yTarget);
		//localizeHere();
		//robot.turnToImmediate(-15);
		robot.moveForwardBy(25);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		//collect 4 more balls
		//for( int i =1; i< 5; i++){
			//collectAnotherBall(i);
		//}

		//robot.moveForwardBy(-25);
		/*robot.getLeftMotor().backward();
		robot.getRightMotor().backward();
		robot.setForwardSpeed(Constants.FORWARD_SPEED);
		*/
		
		
		localizeAtCollection(xTarget, yTarget);
		robot.turnToImmediate(180);
		turnOnObstacleAvoidance();
		//robot.turnToImmediate(15);
		//super.stopCorrectionTimer();
	}

	/**
	 * Corrects the x or y position of the robot after a ball has been collect to compensate
	 * for the wheels kidding
	 * @param xTarget The x ordinate of the target near near the ball collection location
	 * @param yTarget The y ordinate of the target near near the ball collection location
	 */
	private void localizeAtCollection(int xTarget, int yTarget) {
		//odoCorrection.stopCorrectionTimer(false);
		LightSampler left = odoCorrection.getLeftLightSampler();
		LightSampler right = odoCorrection.getRightLightsampler();
		Odometer odometer = robot.getOdometer();
		boolean localized = false;
		robot.moveForwardBy(-30, true);
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

	/**
	 * Turns on obstacle avoidance
	 */
	public void turnOnObstacleAvoidacne(){
		super.turnOnObstacleAvoidance();
	}
	
	/**
	 * Moves the robot to its optimal shooting location
	 */
	public void travelToShootingLocation(){
		odoCorrection.startCorrectionTimer();
		int xTarget = Decoder.shootX;
		int yTarget = Decoder.shootY;
		travelNearShootingLocation();
		//localizeHere();
		super.travelTo(xTarget, yTarget);
		super.travelTo((int)getNearestXInt(), (int)getNearestYInt());
		//localizeHere();
		super.travelTo(xTarget, yTarget);
		//localizeHere();
		super.travelTo(xTarget,yTarget);
		Odometer odometer = robot.getOdometer();
		double currentY = odometer.getY();
		robot.moveForwardBy(-30, true);
		boolean localized = false;
		LightSampler leftLight = odoCorrection.getLeftLightSampler();
		LightSampler rightLight = odoCorrection.getRightLightsampler();
		boolean leftDone = false, rightDone = false;
		while(!localized){
			if(rightLight.isDarkLine() && !leftDone){
				Motor.B.stop();
				leftDone = true;
			}
			if(leftLight.isDarkLine() && !rightDone){
				Motor.A.stop();
				rightDone = true;
			}
			
			if(leftDone && rightDone){
				
				if(Math.abs(odometer.getY() - currentY) > 10){
					odometer.setY(((yTarget/Constants.TILE_DISTANCE_TRUNCATED)*(Constants.TILE_DISTANCE))- Constants.TILE_DISTANCE);
				}
				else{
					odometer.setY(((yTarget/Constants.TILE_DISTANCE_TRUNCATED)*(Constants.TILE_DISTANCE)));
				}
				
				localized = true;
			}
			robot.turnTo(0);
		}

	}
	
	/**
	 * Travels to an intersection location near the shooting location
	 */
	public void travelNearShootingLocation(){
		int xTarget = Decoder.shootX;
		int yTarget = Decoder.shootY;
		//int tilesX = (xTarget + Constants.TILE_DISTANCE_TRUNCATED/2 +2);
		super.travelTo(xTarget, yTarget);
	}
	
	public void localizeHere(){
		odoCorrection.stopCorrectionTimer(SensorSide.LEFT);
		LightSampler left = odoCorrection.getLeftLightSampler();
		left.getLightValue();
		new LightLocalizer(robot, left).doLocalization();
	}
	/**
	 * Shoots the balls at the goal with the {@link Launcher}
	 */
	public void shoot(){
		robot.turnTo(10);
		Launcher.drive(Motor.A, Motor.B, Motor.C);
	}
	
	
	
	
	 /**
		 * Finds nearest x intercept of tiles to {@code travelTo} to relocalize
		 * @return Nearest x intercept
		 */
		private double getNearestXInt(){
			Odometer odometer = robot.getOdometer();
			double currentX = odometer.getX();
			int tilesTravelled = (int)(currentX/30); 
			double overflow = currentX%30;
			if(overflow > 7){
				return (tilesTravelled+1)*Constants.TILE_DISTANCE;
			}
			else {
				return tilesTravelled*Constants.TILE_DISTANCE;
			}
		}
		
		/**
		 * Finds nearest x intercept of tiles to {@code travelTo} to relocalize
		 * @return Nearest Y intercept
		 */	
		private double getNearestYInt(){
			Odometer odometer = robot.getOdometer();
			double currentY = odometer.getY();
			int tilesTravelled = (int)(currentY/30); 
			double overflow = currentY%30;
			if(overflow > 7){
				return (tilesTravelled+1)*Constants.TILE_DISTANCE;
			}
			else {
				return tilesTravelled*Constants.TILE_DISTANCE;
			}
		}
	 
	 
	 
}
