package expermental;

import java.util.LinkedList;
import java.util.Stack;

import odometer.Odometer;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.util.Timer;
import lejos.util.TimerListener;
import main.Constants;

public class OdoCorrecitionTDD implements TimerListener{
	
	private Odometer odometer;
	private LightSensor leftLs;
	private LightSensor rightLs;
	private NXTRegulatedMotor leftMotor;
	private Stack<Double> line, tacoCount;
	private double x, y, theta, taco1, taco2;
	private boolean filter;
	
	private long startTime, endTime;
	
	public static int xCor = 0;
	public static int yCor = 0;
	public static int tCor = 0;
	public static int lsValue = 0;
	public static int rsValue = 0;
	
	private LinkedList<Integer> leftLightValues, rightLightValues;
	
	private Timer correctionTimer;
	
	public OdoCorrecitionTDD(Odometer odometer, LightSensor ls1, LightSensor ls2){
		this.odometer = odometer;
		this.leftLs = ls1;
		this.rightLs = ls2;
		this.leftMotor = Motor.A;
		line = new Stack<Double>();
		tacoCount = new Stack<Double>();
		leftLightValues = new LinkedList<Integer>();
		rightLightValues = new LinkedList<Integer>();
		filter = true;
		correctionTimer = new Timer(Constants.ODOMETER_CORRECTION_TIMEOUT, this);
		resetInternalTimer();
	}

	@Override
	public void timedOut() {
		lsValue = leftLs.getLightValue();
			
		//if left sensor detects a line
		if(isDarkLine(lsValue, this.leftLightValues)){
			Sound.beep();
			//save the time
			resetInternalTimer();
			//store the tacoCount
			taco1 = leftMotor.getTachoCount();
			tacoCount.push(new Double(taco1));
			setOdometerValues();			
		}
		
		rsValue = rightLs.getLightValue();
		
		//if right sensor detects a line
		if(isDarkLine(rsValue, this.rightLightValues)){
			Sound.beep();
			//store the tacoCount
			resetInternalTimer();
			taco2 = leftMotor.getTachoCount(); /*Use left motor to main consistency after obstacle avoidance*/
			tacoCount.push(new Double(taco2));
			setOdometerValues();
		}
		
		
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
		if(Math.abs(x % 30) < 2){
			//correct the x value
			x = ((int)(x/30))* 30;
			xCor++;
			odometer.setX(x);
			lineCheck(x);
		}
		//if the line is y
		else{
			//correct the y value
			y = ((int)(y/30))* 30;
			yCor++;
			odometer.setY(y);
			lineCheck(y);
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
	 * Starts the internal timer for the line detection refresh
	 */
	public void startCorrectionTimer(){
		correctionTimer.start();
	}
	
	public void stopCorrectionTimer(){
		correctionTimer.stop();		
	}
	
	/**
	 * Averages out the  the light sensor values to determine if a line was detected or not 
	 * @param lightSensorValues an array of light values
	 * @return The average of the lightValues
	 */
	public double calculateAverage(LinkedList<Integer> lightSensorValues){
		double value = 0;
		for(int i =0; i< lightSensorValues.size(); i++){
			value += lightSensorValues.get(i);
		}
		return (value/lightSensorValues.size());
	}
	

	/**
	 * Checks if a dark line is detected. If not line is detected then it add the light value
	 * to the sample of light values for the light sensor.
	 * @param lightValue The detected light value
	 * @param lightSensorValues A LinkedList representing the last 10 sampled light values, excluding detection values
	 * @return If a dark line was detected
	 */
	public boolean isDarkLine(int lightValue, LinkedList<Integer> lightSensorValues){
		if(lightSensorValues.size() < 2){ //if there are not enough samples
			if(lightValue < Constants.DARK_LINE_VALUE){
				return true;
			}
			else{
				lightSensorValues.add(lightValue); //add the value to the samples
				return false;
			}
		}
		double lightAverage = calculateAverage(lightSensorValues);
		
		if(percentageCheck(lightValue, lightAverage)){
			return true;
		}
		else{
			populationCheck(lightValue, lightSensorValues);
			return false;
		}		
	}

	/**
	 * @param lightValue The reported light value
	 * @param lightAverage The calculated average light value
	 * @return If the percentage difference is grater than 20 %
	 */
	public boolean percentageCheck(int lightValue, double lightAverage) {
		double decimal = (lightValue-lightAverage)/lightAverage;
		return  (Math.abs(decimal) > 0.2);
	}
	
	/**
	 * Checks the size of the light samples. If there not enough samples,the light value
	 * is added to the list of samples
	 * @param lightValue The current light value obtained
	 * @param lightSensorValues A LinkedListed representing all the light value samples.
	 */
	public void populationCheck(int lightValue, LinkedList<Integer> lightSensorValues){
		if(lightSensorValues.size() < 10){ //if there are not enough samples
			lightSensorValues.add(lightValue); //add the value to the samples
		}
		else{
			lightSensorValues.remove(0); //remove the oldest item from the list
			lightSensorValues.add(lightValue); //add new value to light sample
		}
	}
}
