package navigation;

import main.Constants;
import communication.Decoder;

import robot.OdometryCorrection;
import robot.TwoWheeledRobot;

/**
 * Implements the defensive strategy of the robot
 * @author charles
 *
 */
public class Defence extends Navigation implements Strategy {

	private TwoWheeledRobot robot;
	
	public Defence(TwoWheeledRobot robot, Obstacle obstacle,
			OdometryCorrection odoCorrection) {
		super(robot, obstacle, odoCorrection);
		this.robot = robot;
	}

	/**
	 * The default method called to start the defensive strategy.
	 */
	@Override
	public void start() {
		travelToDefenceLocation();
		super.stopCorrectionTimer();
		robot.turnTo(270);
	}
	
	/**
	 * Moves the robot to a specified location for defence.
	 */
	public void travelToDefenceLocation(){
		int xTarget = Decoder.defenceX;
		int yTarget = Decoder.defenceY;
		super.travelTo(5*Constants.TILE_DISTANCE_TRUNCATED, 5*Constants.TILE_DISTANCE_TRUNCATED);
		super.travelTo(xTarget, yTarget);
		
		
	}

}
