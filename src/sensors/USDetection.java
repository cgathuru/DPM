package sensors;


import main.Constants;
import navigaion.Localization;
import navigaion.Localization.Corner;
import navigaion.Navigation;
import odometer.Odometer;
import odometer.TwoWheeledRobot;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
/**
 * Localizes the robot using the onboard ultrasonic sensor
 * @author charles
 *
 */
public class USDetection implements Runnable {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	private Localization.Corner corner;
	private Odometer odometer;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private Navigation nav;
	
	private double [] pos = new double [3];
	private double angleC = 0;
	private double []angle = new double[2];
	private double theta;

	
	/**
	 * Initializes the classes attributes
	 * @param odo Odomometer
	 * @param us Ultrasonic sensor
	 * @param locType Localization type. Can either be rising or falling edge.
	 */
	public USDetection(Odometer odo, UltrasonicSensor us) {
		this.odometer = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.us = us;
		this.nav = odo.getNavigation();
		corner =  Localization.Corner.BOTTOM_RIGHT;
		
		// switch off the ultrasonic sensor
		us.off();
	}
	
	public void doLocalization(LocalizationType locType, int angleNumber) {
		
		/*Look for angle at which it sees a wall*/
		if (locType == LocalizationType.FALLING_EDGE){
			
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			while(true){
				if(isFacingWall()){
					angle[angleNumber] = odometer.getTheta();
					break;
				}
			}
			
			if(angleNumber == 1){
				doLocalization(LocalizationType.RISING_EDGE, 2);
			}
			else{
				angleC = angle[1] + (360 - angle[2]);
			}
			 
			// update the odometer position (example to follow:)
			odometer.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
			
		} 
		/*Look for angle at which it no longer sees a wall*/
		else {
			
			while(true){
				if(!isFacingWall()){
					angle[angleNumber] = odometer.getTheta();
					break;
				}
			}
			
			if(angleNumber ==1){
				doLocalization(LocalizationType.FALLING_EDGE, 2);
			}
			else{
				angleC = angle[1] + (360 - angle[2]);
			}
			 
				// angleA is clockwise from angleB, so assume the average of the
				// angles to the right of angleB is 45 degrees past 'north'
				
				// update the odometer position (example to follow:)
		}
				
	}

	private boolean isFacingWall() {
		return getFilteredData() < Constants.WALL_DIST;
	}
	
	/**
	 * 
	 * @return The filtered distance between the robot and the detected obstacle
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		switch(corner){
		case BOTTOM_LEFT:
			break;
		case BOTTOM_RIGHT:
			
			break;
		case TOP_LEFT:
			break;
		case TOP_RIGHT:
			break;
		default:
			break;
		}
	}
	
	private void updateHeading(int angle){
		theta = angle;
	}

}
