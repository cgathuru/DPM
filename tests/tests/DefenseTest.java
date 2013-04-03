package tests;

import communication.BluetoothConnection;
import communication.Decoder;
import communication.Transmission;

import navigation.Defence;
import navigation.Launcher;
import navigation.Obstacle;
import navigation.Offence;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightLocalizer;
import sensors.LightSampler;
import sensors.Localiser;
import sensors.USLocalizer;
import utilities.OdoLCD;

public class DefenseTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		UltrasonicSensor usRight = new UltrasonicSensor(SensorPort.S4);
		LightSampler leftLight = new LightSampler(lsLeft);
		LightSampler rightLight = new LightSampler(lsRight);
		USLocalizer us1 = new USLocalizer(patBot, usRight);
		OdometryCorrection odoCorrection = new OdometryCorrection(patBot,leftLight, rightLight);
		Button.waitForAnyPress();
		BluetoothConnection connection = new BluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		Transmission trans = connection.getTransmission();
		Decoder decoder = new Decoder(trans);
		decoder.decodeTranmission();
		Obstacle obstacle = new Obstacle(usRight, usRight, odo, patBot);
		Defence defence = new Defence(patBot, obstacle, odoCorrection, decoder);
		Localiser ls1 = new Localiser(patBot, usRight, leftLight, rightLight, decoder);
		new OdoLCD(odo);
		ls1.dolocalise();
		defence.start();

	}

}
