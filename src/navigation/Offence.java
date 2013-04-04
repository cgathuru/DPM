package navigation;

import lejos.nxt.Motor;
import main.Constants;
import communication.Decoder;

import robot.OdometryCorrection;
import robot.OdometryCorrection.SensorSide;
import robot.TwoWheeledRobot;
import sensors.LightLocalizer;
import sensors.LightSampler;

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
	public Offence(TwoWheeledRobot robot, Obstacle obstacle, OdometryCorrection odoCorrection, Decoder decoder) {
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
		collectBalls();
		super.startCorrectionTimer();
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
		localizeHere();
		//super.travelTo(xTarget, yTarget);
		robot.turnToFace(xTarget, yTarget);
		robot.turnToImmediate(10);
		robot.moveForwardBy(Constants.TILE_DISTANCE);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//collect 4 more balls
		for( int i =1; i< 5; i++){
			collectAnotherBall(i);
		}
		robot.moveForwardBy(-25);
		super.stopCorrectionTimer();
	}

	private void localizeHere() {
		odoCorrection.stopCorrectionTimer(SensorSide.LEFT);
		LightSampler left = odoCorrection.getLeftLightSampler();
//		new LightLocalizer(robot, left).doLocalization();
		//left.getLightValue();
	}
	
	/**
	 * Collects another ball from the ball dispenser
	 */
	public void collectAnotherBall(int i){
		super.moveForwardBy(-3);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.moveForwardBy(3);
	}
	
	/**
	 * Moves the robot to a target destination in line with the ball dispenser so that the robot can approach
	 * the dispenser head on, instead of at an angle.
	 * @param xTarget The x ordinate of the ball dispensers location
	 * @param yTarget The y ordinate of the ball dispenser location
	 */
	public void travelNearCollectionSite(int xTarget, int yTarget){
		//if x ordinate is negative and y is positive
		if(xTarget < 0){
			super.travelTo(0, yTarget);
		}
		//if x ordinate is positive and y is negative
		else if (xTarget > 0 && yTarget < 0){
			super.travelTo(xTarget, 0);
		}
		//if x ordinate is positive and y ordinate is positive
		else{
			super.travelTo(xTarget - 30, yTarget);
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
	 * Moves the robot to its optimal shooting location
	 */
	public void travelToShootingLocation(){
		int xTarget = decoder.shootX;
		int yTarget = decoder.shootY;
		travelNearShootingLocation();
		localizeHere();
		super.travelTo(xTarget, yTarget);
	}
	
	public void travelNearShootingLocation(){
		int xTarget = decoder.shootX;
		int yTarget = decoder.shootY;
<<<<<<< HEAD
		int tilesX = (xTarget + Constants.TILE_DISTANCE_TRUNCATED/2 +2);
=======
<<<<<<< HEAD
		int tilesX = (xTarget+Constants.TILE_DISTANCE_TRUNCATED/2 + 2);
=======
		int tilesX = (xTarget + Constants.TILE_DISTANCE_TRUNCATED/2 +2);
>>>>>>> fuckthis
>>>>>>> extra
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
