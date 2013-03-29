package navigation;

import lejos.nxt.Motor;
import communication.Decoder;

import robot.OdometryCorrection;
import robot.TwoWheeledRobot;

/**
 * Implements the offensive strategy of the robot
 * @author charles
 *
 */
public class Offence extends Navigation implements Strategy{

	/**
	 * Initializes all the parameters needed {@link Navigation}
	 * @param robot A {@link TwoWheeledRobot}
	 * @param obstacle An {@link Obstacle} responsible for obstacle avoidance
	 * @param odoCorrection The {@link OdometryCorrection}
	 */
	public Offence(TwoWheeledRobot robot, Obstacle obstacle, OdometryCorrection odoCorrection) {
		super(robot, obstacle, odoCorrection);
	}
	
	/**
	 * The default method called to start the defensive strategy.
	 */
	@Override
	public void start() {
		collectBalls();
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
		super.stopCorrectionTimer();
		super.travelTo(xTarget, yTarget);
		//collect 4 more balls
		for( int i =1; i< 5; i++){
			collectAnotherBall();
		}
		super.startCorrectionTimer();
	}
	
	public void collectAnotherBall(){
		super.moveForwardBy(-5);
		super.moveForwardBy(5);
	}
	
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
	
	public void turnOffObstalceAvoidance(){
		super.turnOffObstacleAvoidance();
	}

	/**
	 * Moves the robot to its optimal shooting location
	 */
	public void travelToShootingLocation(){
		int xTarget = Decoder.shootX;
		int yTarget = Decoder.shootY;
		super.travelTo(xTarget, yTarget);
	}
	
	
	
	/**
	 * Shoots the balls at the goal with the {@link Launcher}
	 */
	public void shoot(){
		Launcher.drive(Motor.A, Motor.B, Motor.C);
	}
}
