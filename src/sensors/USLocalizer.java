package dpmMaster;


import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
/**
 * Localizes the robot using the onboard ultrasonic sensor
 * @author charles
 *
 */
public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 15;

	private Odometer odo;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private LocalizationType locType;
	private Navigation nav;
	private final int WALL_DIST = 60;
	
	/**
	 * Initializes the classes attributes
	 * @param odo Odomometer
	 * @param us Ultrasonic sensor
	 * @param locType Localization type. Can either be rising or falling edge.
	 */
	public USLocalizer(Odometer odo, UltrasonicSensor us, LocalizationType locType) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.us = us;
		this.locType = locType;
		this.nav = odo.getNavigation();
		
		// switch off the ultrasonic sensor
		us.off();
	}
	
	public void doLocalization() {
		double [] pos = new double [3];
		double angleA = 0, angleB = 0, angleC = 0;
		double theta;
		//detect if facing the wall or away from the wall
		if (getFilteredData() > WALL_DIST) {
			//rotate until a wall is detected, then latch the angle
			robot.setRotationSpeed(ROTATION_SPEED);
			while(true){
				if(getFilteredData() < WALL_DIST){
					Sound.beep();
					//nav.stopMoving();
					angleA = this.odo.getTheta();
					break;
				}
			}
			
			//switch direction and repeat
			robot.setRotationSpeed(-ROTATION_SPEED);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while(true){
				if(getFilteredData() < WALL_DIST){
					Sound.beep();
					angleB = this.odo.getTheta();
					break;
				}
			}
			//calculate the angle needed to turn to
			angleC = angleA - angleB;
			if(angleC < 0) {
				angleC = angleA + (360 - angleB);
			}
			//turn to the angle
			//robot.turnToImmediate(-(angleC / 2 - 45) );
			if(angleC/180 >0){
				angleC = -(360-angleC);
			}
			robot.turnToImmediate(-angleC);
			//nav.turnTo(-angleC -90);
			LCD.drawString("Turning " + -(angleC / 2 - 45), 0, 6);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
							
				//rotating until the robot sees no wall, then latch the angle
				robot.setRotationSpeed(ROTATION_SPEED);
				while(true){
					if(getFilteredData() > WALL_DIST){
						Sound.beep();
						//nav.stopMoving();
						angleA = this.odo.getTheta();
						break;
					}
				}
				
				//Switch direction, keep rotating until the robot sees no wall, then latch the angle
				robot.setRotationSpeed(-ROTATION_SPEED);
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(getFilteredData() > WALL_DIST){
						Sound.beep();
						angleB = this.odo.getTheta();
						break;
					}
				}
				//calculate the angle needed to turn to
				angleC = angleA - angleB;
				if(angleC < 0) {
					angleC = angleA + (360 - angleB);
				}
				nav.turnToImmediate(-(angleC / 2 - 45));
					
				// angleA is clockwise from angleB, so assume the average of the
				// angles to the right of angleB is 45 degrees past 'north'
				
				// update the odometer position (example to follow:)
				odo.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
				
		}
				
	}
	
	/**
	 * 
	 * @return The filtered distance between the robot and the detected obstance
	 */
	private int getFilteredData() {
		int distance;
		
		// do a ping
		us.ping();
		
		// wait for the ping to complete
		try { Thread.sleep(50); } catch (InterruptedException e) {}
		
		// there will be a delay here
		distance = us.getDistance();
				
		return distance;
	}

}
