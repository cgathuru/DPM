package sensors;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
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
	private LightSampler rightLs;
	private LightSampler leftLs;
	private int xOffset;
	private int yOffset;
	private double currentTheta;
	private boolean amIClose;
	/**
	 * Initializes all the variables contained within the class
	 * @param robot The {@link Robot} that controls the robots movements
	 * @param ls Light sensor
	 */
	public LightLocalizer(TwoWheeledRobot robot, LightSampler rightLs, LightSampler leftLs, int xOffset, int yOffset) {
		this.odo = robot.getOdometer();
		this.robot = robot;
		this.rightLs = rightLs;
		this.leftLs = leftLs;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
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
		rightLs.startCorrectionTimer();
		leftLs.startCorrectionTimer();
		double[] angles = new double[4];
		int count = 0;
		currentTheta = odo.getTheta();
		double origX = odo.getX();
		double origY = odo.getY();
		NXTRegulatedMotor leftMotor = robot.getLeftMotor();
		NXTRegulatedMotor rightMotor = robot.getRightMotor();
		robot.turnToImmediate(90);
		align(leftMotor, rightMotor);
		robot.turnToImmediate(90);
		align(leftMotor, rightMotor);
		
				
				
//		while(!sampler.isDarkLine()){
//		robot.moveForward();
//		if((Math.abs(origX-odo.getX())>10) || Math.abs(origY-odo.getY())>10){
//			amIClose=true;
//			break;
//			}
//		}
//		
//		while(!sampler.isDarkLine() || amIClose){
//			robot.moveBackward();
//			if((Math.abs(origX-odo.getX())>10) || Math.abs(origY-odo.getY())>10){
//				break;
//				}
//			}
		
		
		robot.turnTo(330);

	
		
		robot.setRotationSpeed(Constants.ROTATE_SPEED);	
		boolean rotating = true;
		while(rotating){
			//if(ls.getLightValue() < Constants.DARK_LINE_VALUE){
			if(leftLs.isDarkLine()){
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
//		if(Math.abs(currentTheta-newTheta)<10){
		odo.setPosition(new double [] {xDist + xOffset, yDist+yOffset, newTheta}, new boolean [] {true, true, true});
//	}
		//adjust the position to calcuated position
		//robot.turnTo(0);
		leftLs.stopCorrectionTimer();
		rightLs.stopCorrectionTimer();
		
	}

	/**
	 * Aligns the robot to a grid line
	 * @param leftMotor The robots left wheel
	 * @param rightMotor The robots right wheel
	 */
	public void align(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		boolean aligned = false;
		boolean rightDone = false;
		boolean leftDone = false;
		robot.moveForward();
		while(!aligned){
			if(rightLs.isDarkLine() && !rightDone){
				rightMotor.stop();
				rightDone = true;
			}
			if(leftLs.isDarkLine() && !leftDone){
				leftMotor.stop();
				leftDone = true;
			}
			
			if(leftDone && rightDone){
				aligned = true;
			}
		}
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

