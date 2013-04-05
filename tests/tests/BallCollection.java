package tests;

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

public class BallCollection {

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
		Decoder.dispenserX = -30;
		Decoder.dispenserY = 150;
		Decoder.shootX = 120;
		Decoder.shootY = 60;
		Offence attack = new Offence(patBot, obstacle, correction, decoder);

		Localiser localizer= new Localiser(patBot,usLeft, leftLight, rightLight, decoder);
		new LCDInfo(odo);
		Button.waitForAnyPress();

		leftLight.startCorrectionTimer();
		rightLight.startCorrectionTimer();
		odo.startTimer();
		correction.startCorrectionTimer();
		//Decoder.dispenserX = 90;
		//Decoder.dispenserY = 30;
		localizer.dolocalise();
		//attack.start();
		attack.collectBalls();
		attack.travelToShootingLocation();
		attack.stopCorrectionTimer();
		Button.waitForAnyPress();
	}

}
