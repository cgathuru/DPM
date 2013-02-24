package dpmMaster;


import lejos.util.Timer;
import lejos.util.TimerListener;

/**
 * Measures and keeps track of the robots postion
 * @author charles
 *
 */
public class Odometer implements TimerListener {
	public static final int DEFAULT_PERIOD = 25;
	private TwoWheeledRobot robot;
	private Timer odometerTimer;
	private Navigation nav;
	// position data
	private Object lock;
	private double x, y, theta;
	private double [] oldDH, dDH;
	
	/**
	 * Initializes {@code Navigation}
	 * @param robot The robot
	 * @param period The timout period
	 * @param start true if the odometer should be started immidietly
	 */
	public Odometer(TwoWheeledRobot robot, int period, boolean start) {
		// initialize variables
		this.robot = robot;
		this.nav = new Navigation(this);
		odometerTimer = new Timer(period, this);
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
	 * Only shows the setting of the robot
	 * @param robot The robot
	 */
	public Odometer(TwoWheeledRobot robot) {
		this(robot, DEFAULT_PERIOD, false);
	}
	
	/**
	 * Allows for the setting of the robot
	 * @param robot Robot
	 * @param start Boolean to start odometer immidietly
	 */
	public Odometer(TwoWheeledRobot robot, boolean start) {
		this(robot, DEFAULT_PERIOD, start);
	}
	
	/**
	 * Allows for the setting of the robot and the timout period
	 * @param robot The robot
	 * @param period the timout period
	 */
	public Odometer(TwoWheeledRobot robot, int period) {
		this(robot, period, false);
	}
	
	/**
	 * Updates the x and y position, as well as the heading.
	 */
	public void timedOut() {
		robot.getDisplacementAndHeading(dDH);
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
	 * 
	 * @return An instance of the robot class
	 */
	public TwoWheeledRobot getTwoWheeledRobot() {
		return robot;
	}
	
	/**
	 * 
	 * @return An instance of the Navigation class
	 */
	public Navigation getNavigation() {
		return this.nav;
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
	 * Wraps the angle around to prvent having angles larger than 360
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
	 * @param a Current angle
	 * @param b Target Angle
	 * @return The angle to turn to which will minimize the amount of rotation the robot has to do
	 */
	public static double minimumAngleFromTo(double a, double b) {
		double d = fixDegAngle(b - a);
		
		if (d < 180.0)
			return d;
		else
			return d - 360.0;
	}
	/**
	 * 
	 * @return The current heading of the robot
	 */
	
	public double getTheta(){
		return this.theta;
	}
	
	/**
	 * 
	 * @return The current y value of the robot
	 */
	public double getY(){
		return this.y;
	}
	
	/**
	 * 
	 * @return The current x value of the robot
	 */
	public double getX(){
		return this.x;
	}
}
