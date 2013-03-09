package odometer;

import java.util.Stack;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.util.Timer;
import lejos.util.TimerListener;
import main.Constants;
import sensors.LightSampler;

public class OdometryCorrection implements TimerListener{
	
	private Odometer odometer;
	private LightSampler leftLs, rightLs;
	private NXTRegulatedMotor leftMotor;
	private Stack<Double> line, tacoCount;
	private double x, y, theta, taco1, taco2;
	private boolean filter;
	
	private long startTime, endTime;
	private long endTimeLight;
	
	public static long xCor = 0;
	public static long yCor = 0;
	public static int tCor = 0;
	public static int lsValue = Constants.DARK_LINE_VALUE;
	public static int rsValue = Constants.DARK_LINE_VALUE;
	
	
	private Timer correctionTimer;
	
	public OdometryCorrection(Odometer odometer, LightSensor ls1, LightSensor ls2){
		this.odometer = odometer;
		this.leftLs = new LightSampler(ls1);
		this.rightLs = new LightSampler(ls2);
		this.leftMotor = Motor.A;
		line = new Stack<Double>();
		tacoCount = new Stack<Double>();
		filter = true;
		correctionTimer = new Timer(Constants.ODOMETER_CORRECTION_TIMEOUT, this);
		resetInternalTimer();
	}

	@Override
	public void timedOut() {
		
			
		//if left sensor detects a line
		//if(isDarkLine(lsValue, this.leftLightValues)){
		if(leftLs.isDarkLine() && (System.currentTimeMillis() > endTimeLight)){
		  //if(lsValue < Constants.DARK_LINE_VALUE){
			Sound.beep();
			//save the time
			resetInternalTimer();
			//store the tacoCount
			taco1 = leftMotor.getTachoCount();
			tacoCount.push(new Double(taco1));
			
			setOdometerValues();			
		}
		
		
		
		//if right sensor detects a line
		//if(isDarkLine(rsValue, this.rightLightValues)){
		//if(rsValue < Constants.DARK_LINE_VALUE){
		if(rightLs.isDarkLine() && (System.currentTimeMillis() > endTimeLight)){
			Sound.beep();
			//store the tacoCount
			resetInternalTimer();
			taco2 = leftMotor.getTachoCount(); /*Use left motor to main consistency after obstacle avoidance*/
			tacoCount.push(new Double(taco2));
			setOdometerValues();
		}
		lsValue = leftLs.getLightValue();
		rsValue = rightLs.getLightValue();
		
	}//timeout

	/**
	 * Resets the internal line refresh timer
	 */
	public void resetInternalTimer() {
		startTime = System.currentTimeMillis();
		endTime = startTime + Constants.TIME_REFRESH_CONSTANT;
	}

	/**
	 * Sets the odometer x and y values based on the line detected
	 * and sets the value of theta
	 */
	private void setOdometerValues() {
		//get odometer values of x and y
		x = odometer.getX();
		y = odometer.getY();
		// check if the line is x or y
		if(Math.abs(x % 30) < 1){
			//correct the x value
			x = ((int)(x/30))* 30;
			xCor++;
			odometer.setX(x);
			//lineCheck(x);
		}
		//if the line is y
		if(Math.abs(y % 30) < 1){
			//correct the y value
			y = ((int)(y/30))* 30;
			yCor++;
			odometer.setY(y);
			//lineCheck(y);
		}
	}

	/**
	 * Checks the line to determine if the same line has been crossed or not
	 * @param value
	 */
	private void lineCheck(double value) {
		//if timeout has occurred clear the stored line
		if(System.currentTimeMillis() > endTime){
			line.clear();
			filter = true;
		}
		//check if this is first time detecting the line
		if(line.isEmpty()){
			//if it is, add it too the stack
			line.push(new Double(value));
		}
		else{
			//check if it is the same line
			Double valueDouble = new Double(value);
			if(Math.abs(line.peek() - valueDouble) < Constants.MAX_LINE_CORRECTION_BANDWIDTH){
				//calculate the length of the adjacent side
				double diff = valueDouble - line.pop();
				double side = convertTacoToLength(diff);
				//calculate theta
				theta = Math.toDegrees(Math.atan(Constants.WIDTH/ side));
				//set theta
				odometer.setTheta(theta);
				tCor++;
			}
			//if not the same line 
			else{
				//apply filter
				if(filter){
					//turn filter off for next time
					filter = false;
				}
				else{
					//clear the stack
					line.clear();
					//turn the filter back on
					filter = true;
				}
			}
		}
	}

	/**
	 * Converts the tachometer reading into a distance
	 * @param diff The difference in the tachometer count
	 * @return The distance moved
	 */
	public double convertTacoToLength(double diff) {
		return (diff/360)*2*Math.PI*Constants.WHEEL_RADIUS;
	}

	/**
	 * Starts the odometry correction timer and the internal light samplers
	 */
	public void startCorrectionTimer(){
		endTimeLight = System.currentTimeMillis() + Constants.LIGHT_CALIBRATION_TIME;
		this.leftLs.startCorrectionTimer(); //start the left light sampler
		this.rightLs.startCorrectionTimer(); //start the right light sampler
		correctionTimer.start();
	}
	
	/**
	 * Stops the odometry correction timer and the internal light samplers
	 */
	public void stopCorrectionTimer(){
		this.leftLs.stopCorrectionTimer(); //stop the left light sampler
		this.rightLs.stopCorrectionTimer(); //stop the right light sampler
		correctionTimer.stop();		
	}
	
	
}
