package tests;

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

import communication.Decoder;
import communication.StartCorner;
import communication.Transmission;

public class NavTest {

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
		//Navigation nav = new Navigation(patBot, obstacle, correction);
		Offence attack = new Offence(patBot, obstacle, correction);
		Decoder decoder = new Decoder(new Transmission());
		decoder.startCorner = StartCorner.BOTTOM_LEFT;

		Localiser localizer= new Localiser(patBot,usLeft, leftLight, rightLight, decoder);
		new OdoLCD(odo);
		Button.waitForAnyPress();
		
		//leftLight.startCorrectionTimer();
		//rightLight.startCorrectionTimer();
		odo.startTimer();
		Decoder.dispenserX = 90;
		Decoder.dispenserY = 30;
		//localizer.delocalize();
		attack.travelTo(0, 120);
		attack.travelTo(60, 120);
		attack.travelTo(60, 0);
		attack.travelTo(0, 0);
		
		Button.waitForAnyPress();
	}

}
