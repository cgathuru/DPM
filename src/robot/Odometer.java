package robot;


import main.Constants;
import navigation.Navigation;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Timer;
import lejos.util.TimerListener;

/**
 * Measures and keeps track of the robots postion
 * @author charles
 *
 */
public class Odometer implements TimerListener {
	private Timer odometerTimer;
	private NXTRegulatedMotor leftMotor;
	private NXTRegulatedMotor rightMotor;
	// position data
	private Object lock;
	private double x, y, theta;
	private double [] oldDH, dDH;
	
	/**
	 * Initializes {@code Navigation}
	 * @param robot The robot
	 * @param period The timeout period
	 * @param start true if the odometer should be started immediately
	 */
	public Odometer(int period, boolean start) {
		// initialize variables
		odometerTimer = new Timer(period, this);
		this.leftMotor = Motor.A;
		this.rightMotor = Motor.B;
		x = 0.0;
		y = 0.0;
		theta = 0;
		oldDH = new double [2];
		dDH = new double [2];
		lock = new Object();
		
		// start the odometer immediately, if necessary
		if (start)
			odometerTimer.start();
	}
	
	/**
	 * Allows for the setting of the robot
	 * @param start Boolean to start odometer immidietly
	 */
	public Odometer(boolean start) {
		this(Constants.ODOMETER_DEFAULT_PERIOD, start);
	}
	
	/**
	 * Allows for the setting of the robot and the timout period
	 * @param period the timout period
	 */
	public Odometer(int period) {
		this(period, false);
	}
	
	/**
	 * Updates the x and y position, as well as the heading.
	 */
	public void timedOut() {
		getDisplacementAndHeading(dDH);
		dDH[0] -= oldDH[0];
		dDH[1] -= oldDH[1];
		
		// update the position in a critical region
		synchronized (lock) {
			theta += dDH[1];
			theta = fixDegAngle(theta);
			
			x += dDH[0] * Math.sin(Math.toRadians(theta));
			y += dDH[0] * Math.cos(Math.toRadians(theta));
		}
		
		oldDH[0] += dDH[0];
		oldDH[1] += dDH[1];
	}
	
	/**
	 * Gets the position
	 * @param pos The array in which the position information will be stored in.
	 */	
	// accessors
	public void getPosition(double [] pos) {
		synchronized (lock) {
			pos[0] = x;
			pos[1] = y;
			pos[2] = theta;
		}
	}

	/**
	 * Sets the position of the robot
	 * @param pos Position to set to
	 * @param update An array of boolean containing which items would be updated in the robots position
	 */
	// mutators
	public void setPosition(double [] pos, boolean [] update) {
		synchronized (lock) {
			if (update[0]) x = pos[0];
			if (update[1]) y = pos[1];
			if (update[2]) theta = pos[2];
		}
	}
	
	/**
	 * Wraps the angle around to prevent having angles larger than 360
	 * @param angle The current angle
	 * @return The adjusted angle
	 */
	// static 'helper' methods
	public static double fixDegAngle(double angle) {		
		if (angle < 0.0)
			angle = 360.0 + (angle % 360.0);
		
		return angle % 360.0;
	}
	
	/**
	 * Calculates the minimum angle the robot needs to turn to get from from one angle to another
	 * @param currentAngle Current angle
	 * @param targetAngle Target Angle
	 * @return The angle to turn to which will minimize the amount of rotation the robot has to do
	 */
	public static double minimumAngleFromTo(double currentAngle, double targetAngle) {
		double d = fixDegAngle(targetAngle - currentAngle);
		
		if (d < 180.0)
			return d;
		else
			return d - 360.0;
	}
	/**
	 * Gets the current heading of the robot
	 * @return The current heading of the robot
	 */
	public double getTheta(){
		return this.theta;
	}
	
	/**
	 * Gets the roots current y position
	 * @return The current y value of the robot
	 */
	public double getY(){
		return this.y;
	}
	
	/**
	 * Get the robots current x position
	 * @return The current x value of the robot
	 */
	public double getX(){
		return this.x;
	}
	
	/**
	 * Sets the x value of the odometer
	 * @param x x value
	 */
	public void setX(double x){
		synchronized (lock) {
			this.x = x;
		}
	}
	
	/**
	 * Sets the y value of the the odometer
	 * @param y y value
	 */
	public void setY(double y){
		synchronized (lock) {
			this.y = y;
		}
	}
	
	/**
	 * Sets the value of theta
	 * @param theta the angle/heading of the the robot
	 */
	public void setTheta(double theta){
		synchronized (lock) {
			this.theta = theta;
		}
	}
	
	/**
	 * Gets the robots displacement and heading from the motors
	 * @param data An array in which the collected displacement and heading of the robot will be stored
	 */
	public void getDisplacementAndHeading(double[] data){
		int leftTacho, rightTacho;
		leftTacho = leftMotor.getTachoCount();
		rightTacho = rightMotor.getTachoCount();
	
		 
		data[0] = (leftTacho * Constants.WHEEL_RADIUS + rightTacho * Constants.WHEEL_RADIUS) * Math.PI / 360.0;
		data[1] = (leftTacho * Constants.WHEEL_RADIUS - rightTacho * Constants.WHEEL_RADIUS) / Constants.WIDTH;		
		
		
//		data[0] = (leftTacho * Constants.LEFT_WHEEL_RADIUS + rightTacho * Constants.RIGHT_WHEEL_RADIUS) * Math.PI / 360.0;
//		data[1] = (leftTacho * Constants.LEFT_WHEEL_RADIUS - rightTacho * Constants.RIGHT_WHEEL_RADIUS) / Constants.WIDTH;		
		
	}
	
	public void startTimer(){
		odometerTimer.start();
	}
}
