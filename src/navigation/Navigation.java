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
	// put your navigation code here 
	private NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.B;
	//private static double width = 23.4;                                                                               ;
	private double currentX = 0;
	private double currentY = 0;
	private double xTarget = 0;
	private double yTarget = 0;
	private double theta= 0;
	private Odometer odometer;
	private TwoWheeledRobot robot;
	private Obstacle obstacle;
	private OdometryCorrection odoCorrection;
	
	public Navigation(TwoWheeledRobot robot, Obstacle obstacle, OdometryCorrection odoCorrection) {
		this.odometer = robot.getOdometer();
		this.robot = robot;
		this.obstacle = obstacle;
		this.odoCorrection = odoCorrection;
	}
	
	public void travelTo(int xTarget, int yTarget){
		int x = xTarget, y=yTarget;
		robot.turnToFace(x, y);
		
		while(Math.abs(odometer.getX()-x)>2 || Math.abs(odometer.getY()-y)>2){
			
			
			//robot.turnToFace(x, y);
			
			if(Obstacle.filteredLeftDist()>Constants.ObstacleDist && Obstacle.filteredRightDist()>Constants.ObstacleDist){
				//drive straight if there is no obstacle 
				
				robot.setForwardSpeed(Constants.FORWARD_SPEED);
				
			}else{
				//robot.setForwardSpeed(0);
				stopCorrectionTimer();
				obstacle.obManager(); //obManager method called in Obstacle class, exited when robot is clear of obstacle
				//Sound.beep();
				robot.turnToFace(x, y); //problematic, sometimes takes some time to work
				Sound.beep();
				startCorrectionTimer();
			}
		}
		
		
	}
	
	public void update(double x, double y){
		currentX = xTarget;
		currentY = yTarget;
		xTarget = x;
		yTarget = y;
	}
	
	public void moveForwardBy(double distance){
		robot.moveForwardBy(distance);
	}
	
	public double calculateAngle(double x, double y){
		double deltaTheta = Math.toDegrees(Math.atan(y/x));
		if(x!=0 && y!=0){
			return deltaTheta;
		}
		else if(y == 0 && x>0){
			return 0;
		}
		else{
			return 180;
		}
	}
	
	public void moveForward(){
		robot.setForwardSpeed(Constants.FORWARD_SPEED);
	}
	
	
	public boolean isNavigating(){
		double deltaX = xTarget - this.odometer.getX();
		double deltaY = yTarget - this.odometer.getY();
		if(Math.abs(deltaX) < 5 && Math.abs(deltaY) < 5){
			return false;
		}
		
		return true;
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return (int) convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	public double getX(){
		return this.odometer.getY();
	}
	
	public double getY(){
		return this.odometer.getX();
	}
	
	public double getTheta(){
		return this.odometer.getTheta();
	}
	
	public double getCurrentX(){
		return this.currentX;
	}
	
	public double getCurrentY(){
		return this.currentY;
	}
	
	public void startCorrectionTimer(){
		odoCorrection.startCorrectionTimer();
	}
	
	public void stopCorrectionTimer(){
		odoCorrection.stopCorrectionTimer();
	}
}
