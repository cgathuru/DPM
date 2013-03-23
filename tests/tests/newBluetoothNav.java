package tests;

import communication.BluetoothConnection;
import communication.Transmission;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import robot.Odometer;
import robot.TwoWheeledRobot;
import display.LCDInfo;

public class newBluetoothNav {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		//Bluetooth connection with Mufasa (our robot)
		BluetoothConnection connection = new BluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		Transmission trans = connection.getTransmission();
		new LCDInfo(odo);
		
		patBot.travelTo(trans.bx, trans.by);
		Button.waitForAnyPress();

	}

}
