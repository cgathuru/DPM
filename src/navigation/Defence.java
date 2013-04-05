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
	private Decoder decoder;
	
	public Defence(TwoWheeledRobot robot, Obstacle obstacle,
			OdometryCorrection odoCorrection, Decoder decoder) {
		super(robot, obstacle, odoCorrection);
		this.robot = robot;
		this.decoder = decoder;
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
		int xTarget = decoder.defenceX;
		int yTarget = decoder.defenceY;
		super.travelTo(5*Constants.TILE_DISTANCE_TRUNCATED, 5*Constants.TILE_DISTANCE_TRUNCATED);
		super.travelTo(xTarget, yTarget);
		
		
	}

}
