package sensors;

import main.Constants;
import navigaion.Navigation;
import odometer.Odometer;
import odometer.TwoWheeledRobot;
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
public class LightDetection {
	private Odometer odo;
	private TwoWheeledRobot robot;
	private LightSensor ls;
	
	/**
	 * Initialises all the variables contained within the class
	 * @param odo Odometer to get and set positional values
	 * @param ls Light sensor
	 */
	public LightDetection(Odometer odo, LightSensor ls) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.ls = ls;
		
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
		robot.setRotationSpeed(Constants.ROTATE_SPEED);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			if(ls.getLightValue() < Constants.DARK_LINE_VALUE){
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
		double yDist = -(Constants.LIGHT_DIST*Math.cos(Math.toRadians((angles[2] - angles[0])/2)));
		double xDist = -(Constants.LIGHT_DIST*Math.cos(Math.toRadians((angles[3] - angles[1])/2)));
		double deltaTheta = 180 + (angles[3] -angles[1])/2 - angles[3];//angle change
		double newTheta = deltaTheta + odo.getTheta();
		//adjust the position to calcuated position
		odo.setPosition(new double [] {xDist, yDist, newTheta}, new boolean [] {true, true, true});			
		
	}
	
	
}
