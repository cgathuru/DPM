package sensors;

import java.util.LinkedList;

import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.util.Timer;
import lejos.util.TimerListener;
import main.Constants;

/**
 * Collects light values from a light sensor to determine an averaged light value to use to detect a black line.
 * It uses defined number of light samples for its average and a defined sampling rate as defined in the {@link Constants} class
 * @author charles
 *
 */
public class LightSampler implements TimerListener{
	private LightSensor ls;
	private Timer lightTimer;
	
	private LinkedList<Integer> lightSamples;
	
	private double lightAverage;
	private int lightValue;
	
	private int consecutiveDark =0 ;
	
	/**
	 * Initializes the {@code LightSampler} variables such the initial light average value
	 * as defined in the {@code Constants} class as well as the {@code LinkedList} that stores
	 * the sampled light values. 
	 * @param ls
	 */
	public LightSampler(LightSensor ls){
		this.ls = ls;
		lightSamples = new LinkedList<Integer>();
		lightTimer = new Timer(Constants.LIGHT_SAMPLER_REFRESH, this);
		lightAverage = Constants.DARK_LINE_VALUE;
		lightValue = Constants.DARK_LINE_VALUE;;
		//if(ls.isFloodlightOn())
			//ls.setFloodlight(false);
		//for(int i = 0; i< Constants.LIGHT_SAMPLE_SIZE; i++){
			//lightSamples.add(Constants.DARK_LINE_VALUE); //pre-fill samples
		///}
		
	}

	/**
	 * Gets the current light value and calculates a new light value average if the
	 * line detected is not a dark line.
	 */
	@Override
	public void timedOut() {
		lightValue = ls.getLightValue(); //get the light value
		if(!darkLineCheck()){ //if its not a line detection
			populationCheck(); //add light value to the light samples
			calculateAverage(); //calculate the new average
		}
		
		
	}

	/**
	 * Starts the internal timer for the line detection refresh
	 */
	public void startCorrectionTimer(){
		lightTimer.start();
		ls.setFloodlight(true);
	}
	
	/**
	 * Stops the timer for the {@code LightSampler}
	 */
	public void stopCorrectionTimer(){
		lightTimer.stop();
		ls.setFloodlight(false);
	}
	
	/**
	 * Averages out the  the light sensor values to determine if a line was detected or not 
	 * @param lightSensorValues an array of light values
	 * @return The average of the lightValues
	 */
	public void calculateAverage(){
		double value = 0;
		for(int i =0; i< lightSamples.size(); i++){
			value += lightSamples.get(i);
		}
		lightAverage = (value/lightSamples.size());
	}
	
	/**
	 * Checks if a dark line was detected and additionally if there
	 * were consecutive dark lines, so as not to correct the {@link Odometer}
	 * to the wrong value. i.e. When traveling near an intersection and the a y line is detected, the y value on the {@code Odometer}
	 * is adjusted accordingly, however if the robot happens to be traveling the positive y direction, and along the black line,
	 * the {@link OdometeryCorrection} would continue to think that the line is a y line and will continue correcting y (to the wrong value).
	 * The consecutive line check prevents that from happening.
	 * @return True if a dark line was detected less than the consecutive number of times specified
	 * in the {@link Constants} class
	 */
	public boolean isDarkLine(){
		if(consecutiveDark < Constants.CONSECUTIVE_DARK_LINES){
			return darkLineCheck();
		}
		return false;		
	}
	
	/**
	 * If a dark line was detected, as well as keeps track of the number
	 * of consecutive dark lines detected
	 * @return If the percentage difference is greater percentage specified in the {@link Constants} class.
	 */
	public boolean darkLineCheck(){
		double decimal = (lightValue-lightAverage)/lightAverage;
		if(lightSamples.size() < 10){
			if(lightValue < Constants.DARK_LINE_VALUE){
				consecutiveDark++;
				return true;
			}
			else{
				consecutiveDark = 0;
				return false;
			}
		}
		else if(Math.abs(decimal) > Constants.LIGHT_VALUE_PERCENTAGE){
			//Sound.buzz();
			consecutiveDark++;
			return true;
		}
		else{
			consecutiveDark = 0;
			return false;
		}
	}
	
	/**
	 * Checks the size of the light samples. If there not enough samples,the light value
	 * is added to the list of samples
	 * @param lightValue The current light value obtained
	 * @param lightSamples A LinkedListed representing all the light value samples.
	 */
	public void populationCheck(){
		if(lightSamples.size() < Constants.LIGHT_SAMPLE_SIZE){ //if there are not enough samples
			lightSamples.add(lightValue); //add the value to the samples
		}
		else{
			lightSamples.remove(0); //remove the oldest item from the list
			lightSamples.add(lightValue); //add new value to light sample
		}
	}
	
	/**
	 * 
	 * @return The current light value from the light sensor
	 */
	public int getLightValue(){
		return this.lightValue; 
	}
}
