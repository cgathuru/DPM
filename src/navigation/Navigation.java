package navigation;


import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import main.Constants;

/**
 * Controls the flow of events that determines the robots movements
 * @author charles
 *
 */
public class Navigation {
	//private static double width = 23.4;                                                                               ;
	private Odometer odometer;
	private TwoWheeledRobot robot;
	private Obstacle obstacle;
	private OdometryCorrection odoCorrection;
	
	private boolean avoidance;
	
	public Navigation(TwoWheeledRobot robot, Obstacle obstacle, OdometryCorrection odoCorrection) {
		this.odometer = robot.getOdometer();
		this.robot = robot;
		this.obstacle = obstacle;
		this.odoCorrection = odoCorrection;
		avoidance = true;
	}
	
	public void travelTo(int xTarget, int yTarget){
		int x = xTarget, y=yTarget;
		robot.turnToFace(x, y);
		
		while(Math.abs(odometer.getX()-x)>2 || Math.abs(odometer.getY()-y)>2){
			
			
			//robot.turnToFace(x, y);
			
			if(Obstacle.filteredLeftDist()>Constants.ObstacleDist && Obstacle.filteredRightDist()>Constants.ObstacleDist){
				//drive straight if there is no obstacle 
				
				robot.setForwardSpeed(Constants.FORWARD_SPEED);
				
			}
			else if (avoidance){
				//robot.setForwardSpeed(0);
				stopCorrectionTimer();
				obstacle.obManager(); //obManager method called in Obstacle class, exited when robot is clear of obstacle
				//Sound.beep();
				robot.turnToFace(x, y); //problematic, sometimes takes some time to work
				Sound.beep();
				startCorrectionTimer();
			}
			else{
				//do nothing because obstacle avoidance is off.
			}
		}
		
		
	}
	
	/**
	 * Gets the x position of the robot
	 * @return The x-ordinate of the robot
	 */
	public double getX(){
		return this.odometer.getY();
	}
	
	/**
	 * Gets the y position of the robot
	 * @return The y-ordinate of the robot
	 */
	public double getY(){
		return this.odometer.getX();
	}
	
	/**
	 * Gets the heading of the robot
	 * @return The current heading of the robot
	 */
	public double getTheta(){
		return this.odometer.getTheta();
	}
	
	/**
	 * Turns on {@link OdometryCorrection}
	 */
	public void startCorrectionTimer(){
		odoCorrection.startCorrectionTimer();
	}
	
	/**
	 * Turns off {@link OdometryCorrection}
	 */
	public void stopCorrectionTimer(){
		odoCorrection.stopCorrectionTimer();
	}
	
	/**
	 * Turns off obstacle avoidance
	 */
	public void turnOffObstacleAvoidance(){
		this.avoidance = false;
	}
	
	/**
	 * Turns off obstacle avoidance
	 */
	public void turnOnObstacleAvoidance(){
		this.avoidance = true;
	}
	
	/**
	 * Checks if obstacle avoidance is on or off
	 * @return True if the obstacle avoidance is on
	 */
	public boolean getAvoidanceStatus(){
		return this.avoidance;
	}
}
