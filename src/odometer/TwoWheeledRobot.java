package odometer;

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
	
	/**
	 * Sets all controllable attributes of the class
	 * @param leftMotor Left Motor
	 * @param rightMotor Right Motor
	 * @param width wheel-to-wheel distance
	 * @param leftRadius radius of the left wheel
	 * @param rightRadius radius of the right wheel
	 */
	public TwoWheeledRobot(NXTRegulatedMotor leftMotor,
						   NXTRegulatedMotor rightMotor,
						   double width,
						   double leftRadius,
						   double rightRadius) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.leftRadius = Constants.RIGHT_RADIUS;
		this.rightRadius = Constants.RIGHT_RADIUS;
		this.width = Constants.WIDTH;
	}
	
	/**
	 * Sets the left and right motors
	 * @param leftMotor Left motor
	 * @param rightMotor Right motor
	 */
	public TwoWheeledRobot(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this(leftMotor, rightMotor, Constants.WIDTH, Constants.LEFT_RADIUS, Constants.RIGHT_RADIUS);
	}
	
	/**
	 * Sets the motors and the wheel-to-wheel distance
	 * @param leftMotor left motor
	 * @param rightMotor right motor
	 * @param width wheel-to-wheel distance
	 */
	public TwoWheeledRobot(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, double width) {
		this(leftMotor, rightMotor, width, Constants.LEFT_RADIUS, Constants.RIGHT_RADIUS);
	}
	
	// accessors
	/**
	 * 
	 * @return The displacement of the robot from its starting point
	 */
	public double getDisplacement() {
		return (leftMotor.getTachoCount() * leftRadius +
				rightMotor.getTachoCount() * rightRadius) *
				Math.PI / 360.0;
	}
	
	/**
	 * 
	 * @return The current heading of the robot
	 */
	public double getHeading() {
		return (leftMotor.getTachoCount() * leftRadius -
				rightMotor.getTachoCount() * rightRadius) / width;
	}
	
	/**
	 * Gets the  displacement and the heading of the robot from its original position
	 * @param data The array in which the robots displacement and heading are stored in.
	 */
	public void getDisplacementAndHeading(double [] data) {
		int leftTacho, rightTacho;
		leftTacho = leftMotor.getTachoCount();
		rightTacho = rightMotor.getTachoCount();
		
		data[0] = (leftTacho * leftRadius + rightTacho * rightRadius) *	Math.PI / 360.0;
		data[1] = (leftTacho * leftRadius - rightTacho * rightRadius) / width;
	}
	
	// mutators
	/**
	 * Sets the current speed of the motors
	 * @param speed The speed of the motors
	 */
	public void setForwardSpeed(double speed) {
		forwardSpeed = speed;
		setSpeeds(forwardSpeed, rotationSpeed);
	}
	/**
	 * Sets the current rotation speed of the robot
	 * @param speed The rotational speed of the motors
	 */
	public void setRotationSpeed(double speed) {
		rotationSpeed = speed;
		setSpeeds(forwardSpeed, rotationSpeed);
	}
	
	/**
	 * Sets the forard and rotational speeds of the robot
	 * @param forwardSpeed The forward speed
	 * @param rotationalSpeed The rotational speed
	 */
	public void setSpeeds(double forwardSpeed, double rotationalSpeed) {
		double leftSpeed, rightSpeed;

		this.forwardSpeed = forwardSpeed;
		this.rotationSpeed = rotationalSpeed; 

		leftSpeed = (forwardSpeed + rotationalSpeed * width * Math.PI / 360.0) *
				180.0 / (leftRadius * Math.PI);
		rightSpeed = (forwardSpeed - rotationalSpeed * width * Math.PI / 360.0) *
				180.0 / (rightRadius * Math.PI);

		// set motor directions
		if (leftSpeed > 0.0)
			leftMotor.forward();
		else {
			leftMotor.backward();
			leftSpeed = -leftSpeed;
		}
		
		if (rightSpeed > 0.0)
			rightMotor.forward();
		else {
			rightMotor.backward();
			rightSpeed = -rightSpeed;
		}
		
		// set motor speeds
		if (leftSpeed > 900.0)
			leftMotor.setSpeed(900);
		else
			leftMotor.setSpeed((int)leftSpeed);
		
		if (rightSpeed > 900.0)
			rightMotor.setSpeed(900);
		else
			rightMotor.setSpeed((int)rightSpeed);
	}
	
	/**
	 * Converts the distance of the 
	 * @param radius
	 * @param distance
	 * @return
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	/**
	 * Converts an angle into radians from degrees
	 * @param radius Radius of the wheel
	 * @param width Wheel-to-wheel radius
	 * @param angle Heading of the robot
	 * @return The converted angle adjusted for the robot
	 */
	private static int convertAngle(double radius, double width, double angle) {
		return (int) convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	/**
	 * Moves the robot forwrd by an exact distance. Cannot be undone once called
	 * @param distance The distance to move forard by in cm.
	 */
	public void moveForwardBy(double distance){
		leftMotor.rotate(convertDistance(leftRadius, distance), true);
		rightMotor.rotate(convertDistance(rightRadius, distance));
	}
	
	/**
	 * Makes the robot move forward.
	 */
	public void moveForward(){
		setForwardSpeed(forwardSpeed);
	}
	
	/**
	 * Rotates to robot relative to its current positioning
	 * @param theta The angle by which to rotate to
	 */
	public void turnToImmediate(double theta){
		setRotationSpeed(Constants.ROTATE_SPEED);
		leftMotor.rotate( convertAngle(leftRadius, width, theta), true);
		rightMotor.rotate(-convertAngle(rightRadius, width, theta), false);	
	}
	
}
