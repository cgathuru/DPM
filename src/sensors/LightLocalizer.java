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
	private LightSampler sampler;
	private LightSensor ls;
	
	/**
	 * Initializes all the variables contained within the class
	 * @param robot The {@link Robot} that controls the robots movements
	 * @param ls Light sensor
	 */
	public LightLocalizer(TwoWheeledRobot robot, LightSampler ls) {
		this.odo = robot.getOdometer();
		this.robot = robot;
		this.sampler = ls;
		//sampler = new LightSampler(ls);
		
		// turn on the light
		//ls.setFloodlight(true);
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
		double[] angles = new double[4];
		int count = 0;
		robot.turnTo(330);
		robot.setRotationSpeed(Constants.ROTATE_SPEED);	
		boolean rotating = true;
		while(rotating){
			//if(ls.getLightValue() < Constants.DARK_LINE_VALUE){
			if(sampler.isDarkLine()){
				Sound.beep();
				angles[count] = this.odo.getTheta();
				count++;
				if(count == 4){
					rotating = false;
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		robot.setRotationSpeed(0);
		//Calculate the x and y position  and the corresponding using the formuale
		double yDist = -(Constants.LIGHT_DISTANCE*Math.cos(Math.toRadians((angles[2] - angles[0])/2)));
		double xDist = -(Constants.LIGHT_DISTANCE*Math.cos(Math.toRadians((angles[3] - angles[1])/2)));
		double deltaTheta = 180 + (angles[3] -angles[1])/2 - angles[3];//angle change
		double newTheta = deltaTheta + odo.getTheta();
		if(angles[0]-angles[1] <60){
		odo.setPosition(new double [] {xDist + getNearestXInt(), yDist+getNearestYInt(), newTheta}, new boolean [] {true, true, true});
			}
		//adjust the position to calcuated position
		//robot.turnTo(0);
		sampler.stopCorrectionTimer();
		
	}
	
	/**
	 * Finds nearest x intercept of tiles to {@code travelTo} to relocalize
	 * @return Nearest x intercept
	 */
	public double getNearestXInt(){
		double currentX = odo.getX();
		int tilesTravelled = (int)(currentX/30); 
		double overflow = currentX%30;
		if(overflow > 7){
			return (tilesTravelled+1)*Constants.TILE_DISTANCE;
		}
		else {
			return tilesTravelled*Constants.TILE_DISTANCE;
		}
	}
	
	/**
	 * Finds nearest x intercept of tiles to {@code travelTo} to relocalize
	 * @return Nearest Y intercept
	 */	
	public double getNearestYInt(){
		double currentY = odo.getX();
		int tilesTravelled = (int)(currentY/30); 
		double overflow = currentY%30;
		if(overflow > 7){
			return (tilesTravelled+1)*Constants.TILE_DISTANCE;
		}
		else {
			return tilesTravelled*Constants.TILE_DISTANCE;
		}
	}
		
}

