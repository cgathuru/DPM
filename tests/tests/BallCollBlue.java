package tests;

import communication.BluetoothConnection;
import communication.Decoder;
import communication.StartCorner;
import communication.Transmission;
import display.LCDInfo;

import lejos.nxt.Button;
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

public class BallCollBlue {

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

		Localiser localizer= new Localiser(patBot,usLeft, leftLight, rightLight);
		//new LCDInfo(odo);
		Button.waitForAnyPress();
		BluetoothConnection connection = new BluetoothConnection();
		//LCD.clear();
		connection.printTransmission();
		new LCDInfo(odo);
		Transmission trans = connection.getTransmission();
		trans.decodeTranmission();
		Offence attack = new Offence(patBot, obstacle, correction);
		leftLight.startCorrectionTimer();
		rightLight.startCorrectionTimer();
		odo.startTimer();
		//Decoder.dispenserX = 90;
		//Decoder.dispenserY = 30;
		//correction.startCorrectionTimer();
		localizer.dolocalise();
		correction.startCorrectionTimer();
		//attack.start();
		attack.collectBalls();
		attack.travelToShootingLocation();
		attack.stopCorrectionTimer();
		//patBot.turnTo(0);
	//	correction.stopCorrectionTimer();
		
		attack.shoot();
		
		//shoot  a second time
		
		attack.startCorrectionTimer();
		attack.collectBalls();
		attack.travelToShootingLocation();
		attack.stopCorrectionTimer();
		attack.shoot();
		
		Button.waitForAnyPress();
	}

}
