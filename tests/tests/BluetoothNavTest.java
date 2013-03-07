package tests;

import navigaion.Navigation;
import communication.bluetooth.BluetoothConnection;
import communication.bluetooth.Transmission;
import display.LCDInfo;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import odometer.Odometer;
import odometer.TwoWheeledRobot;
import sensors.USLocalizer;

/**
 * This class tests the navigation class and the bluetooth communication class.
 * @author charles
 *
 */
public class BluetoothNavTest {

	/**
	 * Tests if the robot, can receive coordinates and successfully navigate to them.
	 * @param args general
	 */
	public static void main(String[] args) {
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		USLocalizer usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.FALLING_EDGE);
		//Bluetooth connection with Mufasa (our robot)
		BluetoothConnection connection = new BluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		LCDInfo lcd = new LCDInfo(odo);
		Navigation nav = odo.getNavigation();
		nav.travelTo(Transmission.goalX, Transmission.goalY);

	}

}
