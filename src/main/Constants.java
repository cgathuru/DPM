package main;

public final class Constants {

	public static final int TILE_DISTANCE = 30;
	public static final double WHEEL_RADIUS = 2.775;
	public static final double WIDTH = 17; //wheel center
	public static final int ODOMETER_DEFAULT_PERIOD = 25;
	public static final int US_LOCALIZATION_ROTATE_SPEED = 100;
	public static final int ROTATE_SPEED = 150;
	public static final int FORWARD_SPEED = 150;
	public static final int DARK_LINE_VALUE = 44;
	public static final double LIGHT_VALUE_PERCENTAGE = 0.10;
	public static final double LIGHT_DIST = 11; //distance between light sensor and center of robot
	public static final int LIGHT_SAMPLE_SIZE = 20;
	public static final int LIGHT_SAMPLER_REFRESH = 20;
	public static final long LIGHT_CALIBRATION_TIME = 10;
	public static final int CONSECUTIVE_DARK_LINES = 5; //number of consecutive dark lines allowed
	public static final int WALL_DIST = 60; //for ultrasonic sensor
	public static final int US_ANGLE_OFFSET = -25;
	public static final int ODOMETER_CORRECTION_TIMEOUT = 20; //number of miliseconds after which correction is called again
	public static final double ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE = WIDTH/2;
	public static final double MAX_LINE_CORRECTION_BANDWIDTH = 1; //difference between the two 'same' values by each light sensor
	public static final long TIME_REFRESH_CONSTANT = 1500; //amount of time passed before line latched is removed from stack
	public static final String SLAVE_NAME = "Group34";
	public static final int WALL_FOLLWER_REFRESH = 50; //the amount of times passed before the us sensor is re-pinged
	public static final int WALL_FOLLOWER_BANDWIDTH = 3;
	public static final int WALL_FOLLOWER_BANDCENTER = 28; 
	public static final int WALL_FOLLOWER_DELTA = 25; //incremental increase/decrease to wheels speed.
	public static final int WALL_FOLLOWER_FILTER_OUT = 20; //number of readings before considered no wall present
	public static final int WALL_FOLLOWER_MAX_SPEED = 200; //max speed to move wheel at
	public static final int WALL_FOLLOWER_MIN_SPEED = 60;  //min speed to move wheel at
	public static final int ObstacleDist = 25; 
	public static final int motorLow = 170;
	public static final int clearPathDist=80;

}
