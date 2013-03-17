package tests;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import navigaion.Navigation;
import robot.Odometer;
import robot.TwoWheeledRobot;

import communication.BluetoothConnection;
import communication.Transmission;

import display.LCDInfo;

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
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		//Bluetooth connection with Mufasa (our robot)
		BluetoothConnection connection = new BluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		new LCDInfo(odo);
		patBot.travelTo(Transmission.goalX, Transmission.goalY);

	}

}
