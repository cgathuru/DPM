package sensors;

import java.util.LinkedList;

import lejos.nxt.LightSensor;
import lejos.util.Timer;
import lejos.util.TimerListener;
import main.Constants;

public class LightSampler implements TimerListener{
	private LightSensor ls;
	private Timer lightTimer;
	
	private LinkedList<Integer> lightSamples;
	
	private double lightAverage;
	private int lightValue;
	
	public LightSampler(LightSensor ls){
		this.ls = ls;
		lightSamples = new LinkedList<Integer>();
		lightTimer = new Timer(Constants.LIGHT_SAMPLER_REFRESH, this);
		lightAverage = Constants.DARK_LINE_VALUE;
		lightValue = Constants.DARK_LINE_VALUE;
		for(int i = 0; i< Constants.LIGHT_SAMPLE_SIZE; i++){
			lightSamples.add(Constants.DARK_LINE_VALUE); //pre-fill samples
		}
		
	}

	@Override
	public void timedOut() {
		lightValue = ls.getLightValue(); //get the light value
		if(!isDarkLine()){ //if its not a line detection
			populationCheck(); //add light value to the light samples
			calculateAverage(); //calculate the new average
		}
		
		
	}

	/**
	 * Starts the internal timer for the line detection refresh
	 */
	public void startCorrectionTimer(){
		lightTimer.start();
	}
	
	/**
	 * Stops the timer for the {@code LightSampler}
	 */
	public void stopCorrectionTimer(){
		lightTimer.stop();		
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
	 * If a dark line was detected
	 * @return If the percentage difference is greater than 20 %
	 */
	public boolean isDarkLine (){
		double decimal = (lightValue-lightAverage)/lightAverage;
		return  (Math.abs(decimal) > Constants.LIGHT_VALUE_PERCENTAGE);
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
	
	public int getLightValue(){
		return this.lightValue; 
	}
}
