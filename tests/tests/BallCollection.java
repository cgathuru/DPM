package tests;

import communication.Decoder;

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
import sensors.Localizer;
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
		Obstacle obstacle = new Obstacle(usLeft, usRight, odo);
		OdometryCorrection correction = new OdometryCorrection(patBot, leftLight, rightLight);
		Offence attack = new Offence(patBot, obstacle, correction);

		Localizer localizer= new Localizer(patBot,usLeft, leftLight, rightLight);
		new OdoLCD(odo);
		Button.waitForAnyPress();
		
		leftLight.startCorrectionTimer();
		rightLight.startCorrectionTimer();
		odo.startTimer();
		Decoder.dispenserX = 90;
		Decoder.dispenserY = 30;
		localizer.delocalize();
		attack.collectBalls();
		attack.stopCorrectionTimer();
		Button.waitForAnyPress();
	}

}
