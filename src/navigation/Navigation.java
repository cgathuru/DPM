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
	 public static long endLeft, endRight;//used in obstacle avoidance 
	private boolean avoidance;
	
	/**
	 * Initializes all the variables contained within the class
	 * @param robot The {@link TwoWheeledRobot}
	 * @param obstacle An {@link Obstacle} responsible for obstacle avoidance
	 * @param odoCorrection The {@link OdometryCorrection}
	 */
	public Navigation(TwoWheeledRobot robot, Obstacle obstacle, OdometryCorrection odoCorrection) {
		this.odometer = robot.getOdometer();
		this.robot = robot;
		this.obstacle = obstacle;
		this.odoCorrection = odoCorrection;
		avoidance = true;
	}
	
	/**
	 * Travels to a target destination. This version of travelTo includes obstacle avoidance and odometry correction
	 * @param xTarget The x position of the target destination
	 * @param yTarget The y position of the target destination
	 */
//	public void travelTo(int xTarget, int yTarget){
//		int x = xTarget, y=yTarget;
//		robot.turnToFace(x, y);
//		endLeft=System.currentTimeMillis()-Constants.AVOIDANCE_ROUTINE_OFFSET;//
//		endRight=System.currentTimeMillis()-Constants.AVOIDANCE_ROUTINE_OFFSET;//used in obstacle avoidance 
//		while(Math.abs(odometer.getX()-x)>2 || Math.abs(odometer.getY()-y)>2){
//			
//			
//			//robot.turnToFace(x, y);
//			
//			if(Obstacle.filteredLeftDist()>Constants.ObstacleDist && Obstacle.filteredRightDist()>Constants.ObstacleDist){
//				//drive straight if there is no obstacle 
//				
//				robot.setForwardSpeed(Constants.FORWARD_SPEED);
//				
//			}
//			else if (avoidance){
//				//robot.setForwardSpeed(0);
//				stopCorrectionTimer();
//				obstacle.obManager(); //obManager method called in Obstacle class, exited when robot is clear of obstacle
//				//Sound.beep();
//				robot.turnToFace(x, y); //problematic, sometimes takes some time to work
//				Sound.beep();
//				startCorrectionTimer();
//			}
//			else{
//				//do nothing because obstacle avoidance is off.
//			}
//		}
//		
//		
//	}
	
	 public void travelTo(int x, int y){
	     
		  robot.turnToFace(x, y);
		  endLeft=System.currentTimeMillis()-Constants.AVOIDANCE_ROUTINE_OFFSET;//
		  endRight=System.currentTimeMillis()-Constants.AVOIDANCE_ROUTINE_OFFSET;//used in obstacle avoidance 
		  
		  while(Math.abs(odometer.getX()-x)>Constants.ALLOWABLE_ERROR || Math.abs(odometer.getY()-y)>Constants.ALLOWABLE_ERROR){    
		   
		   if((Obstacle.filteredLeftDist()>Constants.OBSTACLE_DIST && Obstacle.filteredRightDist()>Constants.OBSTACLE_DIST)||((Math.cos(Constants.US_ANGLE)*Obstacle.filteredLeftDist())>=(calculateDistance(x,y)-15)&&(Math.cos(Constants.US_ANGLE)*Obstacle.filteredRightDist())>=(calculateDistance(x,y)-15))){
		    //drive straight if there is no obstacle, or if obstacle is farther away than target e.g driving towards ball dispenser
		    robot.setForwardSpeed(Constants.FORWARD_SPEED);   
		    
		   }else{
		    robot.turnToFace(x, y);//
		    robot.setForwardSpeed(0);
		    odoCorrection.stopCorrectionTimer();
		    obstacle.obManager(x,y); //obManager method called in Obstacle class, exited when robot is clear of obstacle        
		    robot.turnToFace(x, y); //done avoiding the obstacle, turn towards the target     
		    odoCorrection.startCorrectionTimer();
		   }
		  }

		 }

	 public double calculateDistance(double x, double y){
	  double deltaX = x - this.odometer.getX();
	  double deltaY = y - this.odometer.getY();
	  double x2 = Math.pow(deltaX, 2);
	  double y2 = Math.pow(deltaY, 2);
	  
	  return Math.sqrt(y2 +x2);
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
