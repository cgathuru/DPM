package sensors;

import java.util.Arrays;

import lejos.nxt.UltrasonicSensor;

/**
 * Handles the Ultrasonic sensor
 * @author charles
 *
 */
public class UsSampler {

	private UltrasonicSensor us;
	private int[] distances;
	
	public UsSampler(UltrasonicSensor us){
		this.us = us;
		distances = new int[5];
		us.continuous();
	}
	
	/**
	 * Gets the filtered distance from the sensor to the obstacle. 
	 * It applies a median filter on an array of 5 distances.
	 * @return The median value from an array of 5 distances
	 */
	public int getFilteredDistance(){	
		us.getDistances(distances, 0, 5);
		Arrays.sort(distances);
		return distances[2];
	}
}
