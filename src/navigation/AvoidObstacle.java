package navigation;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Timer;
import lejos.util.TimerListener;
import main.Constants;

/**
 * Avoids obstacles encountered by the NXT as its navigating to its intended destination.
 * @author charles
 *
 */
public class AvoidObstacle implements TimerListener{
	
	/*
	 * Requires two ultrasonic sensors.
	 */
	private UltrasonicSensor us;
	private Navigation navigation;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.B;
	
	private Timer usTimer;
	
	private final int bandCenter, bandwith;
	
	private int distance = 0, filterControl, currentRightSpeed;
	
	public AvoidObstacle(Navigation navigation, UltrasonicSensor us){
		this.navigation = navigation;
		this.us = us;
		this.usTimer = new Timer(Constants.WALL_FOLLWER_REFRESH, this);
		this.bandCenter = Constants.WALL_FOLLOWER_BANDCENTER;
		this.bandwith = Constants.WALL_FOLLOWER_BANDWIDTH;
		leftMotor.setSpeed(Constants.FORWARD_SPEED);
		rightMotor.setSpeed(Constants.FORWARD_SPEED);
		leftMotor.forward();
		rightMotor.forward();
		currentRightSpeed = 0;
		filterControl = 0;
		
	}

	@Override
	public void timedOut() {
		distance = us.getDistance();
		// rudimentary filter
		if (distance == 255 && filterControl < Constants.WALL_FOLLOWER_FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} 
		else if (distance == 255){
			// true 255, therefore set distance to 255
			this.distance = distance;
		} 
		else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			this.distance = distance;
		}
		// TODO: process a movement based on the us distance passed in (P style)
		currentRightSpeed = rightMotor.getSpeed();
		//if the robot is too close to the wall
		if(this.distance < (this.bandCenter - this.bandwith)) {	
			if(currentRightSpeed >= Constants.WALL_FOLLOWER_MIN_SPEED){ //min speed to move wheel at
			rightMotor.setSpeed((currentRightSpeed - Constants.WALL_FOLLOWER_DELTA)); // calcProp = 1 
			}
		}
		//if the robot is too far from the wall
		else if(this.distance > (this.bandCenter + this.bandwith)){
			if(currentRightSpeed <= Constants.WALL_FOLLOWER_MAX_SPEED){//max speed to move wheel at
				rightMotor.setSpeed((currentRightSpeed + Constants.WALL_FOLLOWER_DELTA));
			}
		}
		else {
			//reduce the wheel speed gradually until its equal to the straight speed 
			if(currentRightSpeed > Constants.FORWARD_SPEED){
				rightMotor.setSpeed(currentRightSpeed - Constants.WALL_FOLLOWER_DELTA);
			}
			else{
				rightMotor.setSpeed(Constants.FORWARD_SPEED);
			}
		}

	}//end timeout
	
}//end class
