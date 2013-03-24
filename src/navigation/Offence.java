package navigation;

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
	}

	/**
	 * Moves the robot to its optimal shooting location
	 */
	public void travelToShootingLocation(){
		
	}
	
	/**
	 * Shoots the balls at the goal with the {@link Launcher}
	 */
	public void shoot(){
		
	}
}
