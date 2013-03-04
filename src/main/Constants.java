package main;

public final class Constants {

	public static final double WHEEL_RADIUS = 2.775;
	public static final double WIDTH = 23.4; //wheel center
	public static final int ROTATE_SPEED = 25;
	public static final int FORWARD_SPEED = 40;
	public static final double DARK_LINE_VALUE = 44;
	public static final double LIGHT_DIST = 11.5; //distance between light sensor and center of robot
	public static final int WALL_DIST = 60; //for ultrasonic sensor
	public static final int ODOMETER_CORRECTION_TIMEOUT = 20; //number of miliseconds after which correction is called again
	public static final double MAX_LINE_CORRECTION_BANDWIDTH = 1; //difference between the two 'same' values by each light sensor
	public static final long TIME_REFRESH_CONSTANT = 2000; //amount of time passed before line latched is removed from stack
	public static final String SLAVE_NAME = "Group34";

}
