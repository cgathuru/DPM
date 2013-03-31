package main;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import navigation.Navigation;
import navigation.Obstacle;
import navigation.Offence;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightSampler;
import sensors.Localiser;
import sensors.USLocalizer;
import utilities.OdoLCD;

import communication.BluetoothConnection;
import communication.Decoder;

import display.LCDInfo;

/**
 * Contains the main method.
 * @author charles
 *
 */
public class Dpm {

	/**
	 * Main method of the robot. It calls the {@link Initializer} 
	 * which initialises all of the necessary components and starts a round of the game
	 * @param args
	 */
	public static void main(String[] args) {
		new Initializer().run();
		Button.waitForAnyPress();
	}

}
