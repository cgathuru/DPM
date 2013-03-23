package sensors;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import main.Constants;
import navigation.Navigation;
import robot.Odometer;
import robot.TwoWheeledRobot;

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
	private LightSampler sampler;
	
	/**
	 * Initializes all the variables contained within the class
	 * @param robot The {@link Robot} that controls the robots movements
	 * @param ls Light sensor
	 */
	public LightLocalizer(TwoWheeledRobot robot, LightSensor ls) {
		this.odo = robot.getOdometer();
		this.robot = robot;
		this.ls = ls;
		sampler = new LightSampler(ls);
		
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
		sampler.startCorrectionTimer();
		LCD.drawString("Light Value: " + ls.getLightValue(), 0, 4);
		double[] angles = new double[4];
		int count = 0;
		robot.setRotationSpeed(Constants.ROTATE_SPEED);		
		while(true){
			//if(ls.getLightValue() < Constants.DARK_LINE_VALUE){
			if(sampler.isDarkLine()){
				Sound.beep();
				angles[count] = this.odo.getTheta();
				count++;
				if(count == 4){
					break;
				}
				
				
				try {
					Thread.sleep(500);
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
		//robot.turnTo(0);
		sampler.stopCorrectionTimer();
		
	}
	
	
}
