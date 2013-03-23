package tests;

import navigation.Launcher;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import robot.Odometer;
import robot.TwoWheeledRobot;

import communication.BluetoothConnection;
import communication.Transmission;

import display.LCDInfo;

public class BlutoothLocNav {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int goalX = 5*30;
		int goalY = 10*30;
		int goalTargetX = 5*30;
		int goalTargetY = 2*30;
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		//Bluetooth connection with Mufasa (our robot)
		BluetoothConnection connection = new BluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		Transmission trans = connection.getTransmission();
		new LCDInfo(odo);
		patBot.travelTo(goalTargetX, goalTargetY);
		patBot.turnToFace(goalTargetX, goalTargetY);
		Button.waitForAnyPress();

	}

}
