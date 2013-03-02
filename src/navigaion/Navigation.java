package navigaion;


import odometer.Odometer;
import odometer.TwoWheeledRobot;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import main.Constants;


public class Navigation {
	// put your navigation code here 
	private NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.B;
	private static double width = 23.4;                                                                               ;
	private double currentX = 0;
	private double currentY = 0;
	private double xTarget = 0;
	private double yTarget = 0;
	private double theta= 0;
	private Odometer odometer;
	private TwoWheeledRobot robot;
	
	
	public Navigation(Odometer odo) {
		this.odometer = odo;
		this.robot = odo.getTwoWheeledRobot();
	}
	
	public void travelTo(double x, double y){
		update(x,y);
		double distance = calculateDistance(x,y);
		double deltaX = x - odometer.getX();
		double deltaY = y - odometer.getY();	
		autoRotate(deltaX, deltaY);
		
		moveForwardBy(distance);
		
		
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
	
	public double calculateRelativeAngle(double xTarget, double yTarget){
		double deltaX = xTarget - currentX;
		double deltaY = yTarget - currentY;
		
		double angle = Math.toDegrees(Math.atan(deltaY/deltaX));
		return angle;
	}
	
	public void autoRotate(double x, double y){
		double deltaTheta = Math.toDegrees(Math.atan(y/x));
		double theta = deltaTheta;
		
		rotationCorrection(x , y , theta);
		
	}
	
	private void rotationCorrection(double x, double y, double theta){
		if(y == 0 && x > 0){
			turnTo(0);
		}
		else if(y == 0 && x < 0){
			turnTo(180);
		}
		else if(x == 0 && y > 0){
			turnTo(90);
		}
		else if(x == 0 && y < 0){
			turnTo(270);
		}
		else{
			turnTo(theta);
		}
	}
	
	public void autoRotate(double xTarget, double yTarget, double xPos, double yPos){
		double deltaX = xTarget - xPos;
		double deltaY = yTarget - yPos;
		double theta = Math.toDegrees(Math.atan(deltaY/deltaX));
		
		rotationCorrection(deltaX, deltaY, theta);
	}
	
	public void turnToImmediate(double theta){
		robot.setRotationSpeed(Constants.ROTATE_SPEED);
		leftMotor.rotate( convertAngle(Constants.LEFT_RADIUS, width, theta), true);
		rightMotor.rotate(-convertAngle(Constants.RIGHT_RADIUS, width, theta), false);	
	}
	
	
	public double calculateDistance(double x, double y){
		double deltaX = x - this.odometer.getX();
		double deltaY = y - this.odometer.getY();
		double x2 = Math.pow(deltaX, 2);
		double y2 = Math.pow(deltaY, 2);
		
		return Math.sqrt(y2 +x2);
	}

	public void stopMoving(){
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
		rightMotor.stop();
		leftMotor.stop();
	}
	
	public void startMoving(){
		rightMotor.forward();
		leftMotor.forward();
	}


	
	public void turnTo(double angle){
		
		double robotAngle = odometer.getTheta();
		double theta = 0;
		double deltaTheta = angle - robotAngle;
		if(Math.abs(deltaTheta) <= 180){
			theta = deltaTheta;
		}
		else if(deltaTheta < -180){
			theta = deltaTheta + 360;
		}
		else if(deltaTheta > 180){
			theta = deltaTheta -360;
		}
		
		robot.turnToImmediate(theta);
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
}
