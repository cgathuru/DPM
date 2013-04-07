package main;

/**
 * This class contains the constants used in the code.
 * @author charles
 *
 */
public final class Constants {
	
	public static final boolean[][] PLAYING_FEILD = new boolean[12][12]; 

	/**
	 * Number of intersection points to the right of the bottom left corner
	 */
	public static final int GOALX = 5;
	
	/**
	 * Number of intersection points in front of the bottom left corner
	 */
	public static final int GOALY = 10;
	
	/** 
	 * Distance between the tiles
	 */
	public static final double TILE_DISTANCE = 30.38;
	
	/**
	 * The integer version of the tile distance used for the {@link Decoder}
	 */
	public static final int TILE_DISTANCE_TRUNCATED = (int) TILE_DISTANCE;
	/**
	 * The radius of the robots wheels
	 */
	public static final double WHEEL_RADIUS = 2.68;
	/**
	 * The wheel to wheel radius of the robot
	 */
	public static final double WIDTH = 16.33; //wheel center
	/**
	 * The default {@link Odometer} timeout period
	 */
	public static final int ODOMETER_DEFAULT_PERIOD = 25;
	/**
	 * The rotation speed of the robot {@link USLocalizer}
	 */
	public static final int US_LOCALIZATION_ROTATE_SPEED = 100;
	/**
	 * The default rotation speed of the robot
	 */
	public static final int ROTATE_SPEED = 200;
	/**
	 * The default forawrd speed of the robot
	 */
	public static final int FORWARD_SPEED = 200;
	/**
	 * The default light value for detecting a dark line
	 */
	public static final int DARK_LINE_VALUE = 44;
	/**
	 * Minimum percentage difference needed between the light average and the current light value
	 * in order for the system to detect a black line in the {@link LightSampler}
	 */
	public static final double LIGHT_VALUE_PERCENTAGE = 0.11;
	/**
	 * The distance between the sensor and the center of the robot needed for light localization
	 */
	public static final double LIGHT_DISTANCE = 12; //distance between light sensor and center of robot
	/**
	 * The number of light samples to be averaged when by the {@link LightSampler}
	 */
	public static final int LIGHT_SAMPLE_SIZE = 20;
	/**
	 * The number of milliseconds after which the the {@link LightSampler} {@code timedOut} function called again 
	 */
	public static final int LIGHT_SAMPLER_REFRESH = 20;

	/**
	 * The number of consecutive dark lines detected before the {@link LightSampler} determines that it is traveling on a path that it thinks is a dark. Once this number is reaached the {@code isDarkLine}
	 * method will return false.
	 */
	public static final int CONSECUTIVE_DARK_LINES = 5; //number of consecutive dark lines allowed
	/**
	 * Distance used by the Ultrasonic sensor to determine if the wall is detected
	 */
	//public static final int WALL_DIST = 60; //for ultrasonic sensor
	public static final int WALL_DIST  = 50;
	public static final int US_ANGLE_OFFSET = -26;
	/**
	 * The amount of time passed before the {@code OdometryCorrection} is ran again
	 */
	public static final int ODOMETER_CORRECTION_TIMEOUT = 20; //number of miliseconds after which correction is called again
	/**
	 * The distance used by {@link OdometryCorrection} when determining which line the sensor has detected
	 */
	public static final double ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE = WIDTH/2;
	/**
	 * Difference between the two 'same' values by each light sensor
	 */
	public static final double MAX_LINE_CORRECTION_BANDWIDTH = 1; //difference between the two 'same' values by each light sensor
	/**
	 * Amount of time before a detected line is removed from the stack in {@link OdometryCorrection}
	 */
	public static final long TIME_REFRESH_CONSTANT = 1500; //amount of time passed before line latched is removed from stack
	
	public static final int ObstacleDist = 30; 
	public static final int motorLow = 170;
	public static final int clearPathDist=80;
	 public static final int OBSTACLE_DIST = 30;
	 public static final int CLEARPATH_DIST = 80;
	 public static final int ALLOWABLE_ERROR = 2;
	 public static final int US_ANGLE = 25;
	 public static final int AVOIDANCE_ROUTINE_EXPIRATION=20000; //time for obstacle avoidance routine to expire in milliseconds
	 public static final int AVOIDANCE_ROUTINE_OFFSET = 50000; //offset time to make avoidance routine expired
	 public static final int MAX_TURN_TIME = 3300;
	 public static final int MAX_FORWARD_TIME = 6000;
	 public static final int OBSTACLE_DIST2 = 25;
	 public static final int NO_PATH_DIST = 45;
	 public static final int MIN_TURN_TIME =1500;
	 
	 
	 
	  public static final int WALL_FOLLWER_REFRESH = 50; //the amount of times passed before the us sensor is re-pinged
	  public static final int WALL_FOLLOWER_BANDWIDTH = 3;
	  public static final int WALL_FOLLOWER_BANDCENTER = 28; 
	  public static final int WALL_FOLLOWER_DELTA = 25; //incremental increase/decrease to wheels speed.
	  public static final int WALL_FOLLOWER_FILTER_OUT = 20; //number of readings before considered no wall present
	  public static final int WALL_FOLLOWER_MAX_SPEED = 200; //max speed to move wheel at
	  public static final int WALL_FOLLOWER_MIN_SPEED = 60;  //min speed to move wheel at
	  public static final String SLAVE_NAME = "Android16";
	  public static final int SIDE_DISTANCE = 30;//distance to the right or left
	  public static final int FORWARD_DISTANCE = 60; //forward distance
	  public static final int OBSTACLE_DISTANCE_WHILE_AVOIDING = 20;
	  public static final int OBSTACLE_NOT_THERE_DISTANCE = 90;
	  public static final int BOTTOM_LEFT_X = -30;
	  public static final int BOTTOM_LEFT_Y = -30;
	  public static final int BOTTOM_RIGHT_X = 330;
	  public static final int BOTTOM_RIGHT_Y = -30;
	  public static final int TOP_LEFT_X = -30;
	  public static final int TOP_LEFT_Y = 330;
	  public static final int TOP_RIGHT_X = 330;
	  public static final int TOP_RIGHT_Y = 330; 

	 }

	 


