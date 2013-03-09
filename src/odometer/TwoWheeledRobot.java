package odometer;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import main.Constants;

/**
 * Controls the wheel motors of the robot.
 * 
 * Implements core navigation functionality
 * @author charles
 *
 */
public class TwoWheeledRobot {
	
	private NXTRegulatedMotor leftMotor, rightMotor;
	private double leftRadius, rightRadius, width;
	private double forwardSpeed = Constants.FORWARD_SPEED, rotationSpeed = Constants.ROTATE_SPEED;
	private double currentX = 0;
	private double currentY = 0;
	private double xTarget = 0;
	private double yTarget = 0;
	private double theta= 0;
	private boolean isRotating;
	
	/**
	 * Sets all controllable attributes of the class
	 * @param leftMotor Left Motor
	 * @param rightMotor Right Motor
	 * @param width wheel-to-wheel distance
	 * @param leftRadius radius of the left wheel
	 * @param rightRadius radius of the right wheel
	 */
	public TwoWheeledRobot(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.leftRadius = Constants.WHEEL_RADIUS;
		this.rightRadius = Constants.WHEEL_RADIUS;
		this.width = Constants.WIDTH;
		isRotating = false;
		
	}
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	
	}
	
	public void turnToImmediate(double theta){
		isRotating = true;
		leftMotor.rotate(convertAngle(leftRadius,width,theta),true);
		rightMotor.rotate(-convertAngle(rightRadius,width,theta),false);
	}
	
	public void moveForwardBy(double distance){
		isRotating = false;
		rightMotor.setSpeed(Constants.FORWARD_SPEED);
		leftMotor.setSpeed(Constants.FORWARD_SPEED);
		leftMotor.forward();
		rightMotor.forward();
		leftMotor.rotate(convertDistance(leftRadius,distance), true);
		rightMotor.rotate(convertDistance(rightRadius, distance), false);
		
	}

	public void getDisplacementAndHeading(double[] data){
		int leftTacho, rightTacho;
		leftTacho = leftMotor.getTachoCount();
		rightTacho = rightMotor.getTachoCount();
	
		 
		data[0] = (leftTacho * leftRadius + rightTacho * rightRadius) * Math.PI / 360.0;
		data[1] = (leftTacho * leftRadius - rightTacho * rightRadius) / width;		
	}
	
	 public void setForwardSpeed(int speed) {
		 isRotating = false;
		 leftMotor.setSpeed(speed);
		 rightMotor.setSpeed(speed);
		 leftMotor.forward();
		 rightMotor.forward();
		 }

	 public void setRotationSpeed(double speed){
		 isRotating = true;
		 rightMotor.setSpeed((int)speed);
		 leftMotor.setSpeed((int)speed);
		 if (speed>0){
			 leftMotor.forward();
			 rightMotor.backward();
		 }
		 else{
			 leftMotor.backward();
			 rightMotor.forward();
		 }
	 }
	 public void motorFloat(){
		 leftMotor.flt();
		 rightMotor.flt();
	 }
	 
	 /**
	  * 
	  * @return True if the robot is rotating
	  */
	 public boolean isRotating(){
		 return this.isRotating;
	 }
}

	 
	 	
	 
	 
	
	









	
	

