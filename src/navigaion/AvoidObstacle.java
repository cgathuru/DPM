package navigaion;

import lejos.nxt.UltrasonicSensor;

/**
 * Avoids obstacles encountered by the NXT as its navigating to its intended destination.
 * @author charles
 *
 */
public class AvoidObstacle {
	
	/*
	 * Requires two ultrasonic sensors.
	 */
	private UltrasonicSensor usLeft, usRight;
	private Navigation navigation;
	
	public AvoidObstacle(Navigation navigation, UltrasonicSensor usLeft){
		this.navigation = navigation;
		this.usLeft = usLeft;
		
	}
}
