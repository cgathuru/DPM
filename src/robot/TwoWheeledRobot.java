package robot;

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
	private double currentTachoCount;
	private double previousTachoCount;
	private Odometer odo;
	
	/**
	 * Sets all controllable attributes of the class
	 * @param leftMotor Left Motor
	 * @param rightMotor Right Motor
	 */
	public TwoWheeledRobot(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, Odometer odo) {
		this.odo = odo;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.leftRadius = Constants.WHEEL_RADIUS;
		this.rightRadius = Constants.WHEEL_RADIUS;
		this.width = Constants.WIDTH;
		isRotating = false;
		
		
	}
	
	/**
	 * Converts a distance into the number degreess the wheel needds to turn to order to
	 * move the given distance
	 * @param radius The wheel radius
	 * @param distance The distance need to be moved
	 * @return The number of degrees of rotation of the wheels needed to achieve the required distance
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	/**
	 * Converts an angle in degrees the wheel needs to rotate in order face a
	 * given heading 
	 * @param radius The wheel radius of the robot
	 * @param width The wheel center of the robot
	 * @param angle The angle which the robot to should turn to face
	 * @return The number of degrees the wheel needs to rotate
	 */
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	
	}
	
	/**
	 * Turns robot by the given number of degrees anti-clockwise
	 * @param theta The angle in degrees which the robot should rotate by
	 */
	
	public void turnToImmediate(double theta){
		isRotating = true;
		leftMotor.setSpeed(Constants.ROTATE_SPEED);
		rightMotor.setSpeed(Constants.ROTATE_SPEED);
		leftMotor.forward();
		rightMotor.forward();
		leftMotor.rotate(convertAngle(leftRadius,width,theta),true);
		rightMotor.rotate(-convertAngle(rightRadius,width,theta),false);
	}
	
	public void travelTo(int x, int y){
		double currentX= odo.getX();
		double currentY = odo.getY();
		turnTo(currentX, currentY, x, y);
		
		//
		
		
		
	}
	
//	public double calculateDistance(double x, double y){
//		double deltaX = x - odo.getX();
//		double deltaY = y - odo.getY();
//		double x2 = Math.pow(deltaX, 2);
//		double y2 = Math.pow(deltaY, 2);
//		
//		return Math.sqrt(y2 +x2);
//	}
//NOT TECHNICALLY USED LATER ON	
	
	
	public void turnTo(double currentX, double currentY, int targetX, int targetY){
		double deltaX = targetX -currentX;
		double deltaY = targetY - currentY;
		
		double angle = 90- Math.toDegrees(Math.atan2(deltaY, deltaX));
		double robotAngle = odo.getTheta();
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
		
		turnToImmediate(theta);
	}
	
	
	
	
	
	/**
	 * Moves the robot forward  in a straight line by a given distance
	 * @param distance The distance to moved in centimeters
	 */
	public void moveForwardBy(double distance){
		isRotating = false;
		rightMotor.setSpeed(Constants.FORWARD_SPEED);
		leftMotor.setSpeed(Constants.FORWARD_SPEED);
		leftMotor.forward();
		rightMotor.forward();
		leftMotor.rotate(convertDistance(leftRadius,distance), true);
		rightMotor.rotate(convertDistance(rightRadius, distance), false);
		
	}

	/**
	 * Gets the robots displacement and heading from the motors
	 * @param data An array in which the collected displacement and heading of the robot will be stored
	 */
	public void getDisplacementAndHeading(double[] data){
		int leftTacho, rightTacho;
		leftTacho = leftMotor.getTachoCount();
		rightTacho = rightMotor.getTachoCount();
	
		 
		data[0] = (leftTacho * leftRadius + rightTacho * rightRadius) * Math.PI / 360.0;
		data[1] = (leftTacho * leftRadius - rightTacho * rightRadius) / width;		
	}
	
	/**
	 * Sets the robots forward speed to the given value
	 * @param speed The speed at which the robot should move forward at
	 */
	 public void setForwardSpeed(int speed) {
		 isRotating = false;
		 leftMotor.setSpeed(speed);
		 rightMotor.setSpeed(speed);
		 leftMotor.forward();
		 rightMotor.forward();
	}

	 /**
	  * Sets the speed at which the robot should rotate at.
	  * @param speed The speed at which the robot should rotate at
	  */
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
	 
	 /**
	  * Floats the robots motors
	  */
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
	 
	 /**
	  * Checks if the robot is moving
	  * @return True of the robot is moving
	  */
	 public boolean isMoving(){
		 return true;
	 }
	 
}

	 
	 	
	 
	 
	
	









	
	

