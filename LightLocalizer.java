package dpmMaster;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.Sound;

/**
 * This class  contains the light localizer class that helps the robot determine it current position accurately.
 * The robot will rotate about a fixed axis and determine its position.
 * @author charles
 *
 */
public class LightLocalizer {
	private Odometer odo;
	private TwoWheeledRobot robot;
	private LightSensor ls;
	private double sensorDist = 11.5;
	private final double FORWARD_SPEED = 30;
	private final double DARK_LINE_VALUE = 45;
	private Navigation nav;
	public static double ROTATION_SPEED = 15;
	private final double LIGHT_DIST = 11.5;
	
	/**
	 * Initialises all the variables contained within the class
	 * @param odo Odometer to get and set positional values
	 * @param ls Light sensor
	 */
	public LightLocalizer(Odometer odo, LightSensor ls) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.ls = ls;
		this.nav = odo.getNavigation();
		
		// turn on the light
		ls.setFloodlight(true);
	}
	
	/**
	 * Initializes the light sensor localization.
	 */
	public void doLocalization() {
		// drive to location listed in tutorial
		// start rotating and clock all 4 gridlines
		// do trig to compute (0,0) and 0 degrees
		// when done travel to (0,0) and turn to 0 degrees
		LCD.drawString("Light Value: " + ls.getLightValue(), 0, 4);
		double[] angles = new double[4];
		double theta = odo.getTheta();
		int count = 0;
		robot.setRotationSpeed(ROTATION_SPEED);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			if(ls.getLightValue() < DARK_LINE_VALUE){
				Sound.beep();
				angles[count] = this.odo.getTheta();
				count++;
				if(count == 4){
					break;
				}
				
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		robot.setRotationSpeed(0);
		//Calculate the x and y position  and the corresponding using the formuale
		double yDist = -(LIGHT_DIST*Math.cos(Math.toRadians((angles[2] - angles[0])/2)));
		double xDist = -(LIGHT_DIST*Math.cos(Math.toRadians((angles[3] - angles[1])/2)));
		double deltaTheta = 180 + (angles[3] -angles[1])/2 - angles[3];//angle change
		double newTheta = deltaTheta + odo.getTheta();
		//adjust the position to calcuated position
		odo.setPosition(new double [] {xDist, yDist, newTheta}, new boolean [] {true, true, true});
		nav.travelTo(0, 0);
		//nav.turnTo(0);
		
		
			
		
		
		
		
	}
	
	
}
