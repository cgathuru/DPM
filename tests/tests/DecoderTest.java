package tests;

import robot.Odometer;
import robot.TwoWheeledRobot;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

import communication.BluetoothConnection;
import communication.Decoder;

public class DecoderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Odometer odometer = new Odometer(false);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odometer, Motor.A, Motor.B);
		Button.waitForAnyPress();
		BluetoothConnection connection = new BluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		odometer.startTimer();
		patBot.travelTo(Decoder.dispenserX, Decoder.dispenserY);
		
	}

}
