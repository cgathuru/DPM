package tests;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import navigation.Obstacle;
import navigation.Offence;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.LightSampler;
import sensors.Localiser;
import utilities.OdoLCD;

import communication.BluetoothConnection;
import communication.Decoder;
import communication.StartCorner;
import communication.Transmission;

public class BlueLocCollect {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NXTRegulatedMotor leftMotor = Motor.A;
		NXTRegulatedMotor rightMotor = Motor.B;
		Odometer odo = new Odometer(false);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		UltrasonicSensor usLeft = new UltrasonicSensor(SensorPort.S3);
		UltrasonicSensor usRight = new UltrasonicSensor(SensorPort.S4);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		LightSampler leftLight = new LightSampler(lsLeft);
		LightSampler rightLight = new LightSampler(lsRight);
		Obstacle obstacle = new Obstacle(usRight, usLeft, odo, patBot);
		OdometryCorrection correction = new OdometryCorrection(patBot, leftLight, rightLight);
		Decoder decoder = new Decoder(new Transmission());
		decoder.startCorner = StartCorner.BOTTOM_LEFT;
		Offence attack = new Offence(patBot, obstacle, correction, decoder);
		Localiser localizer= new Localiser(patBot,usLeft, leftLight, rightLight, decoder);
		new OdoLCD(odo);
		Button.waitForAnyPress();
		BluetoothConnection connection = new BluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		Transmission trans = connection.getTransmission();
		new Decoder(trans).decodeTranmission();
		leftLight.startCorrectionTimer();
		rightLight.startCorrectionTimer();
		odo.startTimer();
		localizer.dolocalise();
		attack.collectBalls();
		attack.stopCorrectionTimer();
		Button.waitForAnyPress();
	}

}
