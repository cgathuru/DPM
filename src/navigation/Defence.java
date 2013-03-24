package navigation;

import robot.OdometryCorrection;
import robot.TwoWheeledRobot;

/**
 * Implements the defensive strategy of the robot
 * @author charles
 *
 */
public class Defence extends Navigation implements Strategy {

	public Defence(TwoWheeledRobot robot, Obstacle obstacle,
			OdometryCorrection odoCorrection) {
		super(robot, obstacle, odoCorrection);
	}

	/**
	 * The default mthod called to start the defensive strategy.
	 */
	@Override
	public void start() {
		travelToDefenceLocation();
		super.stopCorrectionTimer();
	}
	
	/**
	 * Moves the robot to a specified location for defence.
	 */
	public void travelToDefenceLocation(){
		
	}

}
