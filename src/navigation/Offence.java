package navigation;

import robot.OdometryCorrection;
import robot.TwoWheeledRobot;

public class Offence extends Navigation implements Strategy{

	public Offence(TwoWheeledRobot robot, Obstacle obstacle, OdometryCorrection odoCorrection) {
		super(robot, obstacle, odoCorrection);
	}

	@Override
	public void start() {
		collectBalls();
		travelToShootingLocation();
		shoot();
		
	}
	
	public void collectBalls(){
	}

	public void travelToShootingLocation(){
		
	}
	
	public void shoot(){
		
	}
}
