package tests;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import robot.Odometer;
import robot.TwoWheeledRobot;
import sensors.LightSampler;
import sensors.Localiser;
import utilities.OdoLCD;

import communication.Decoder;
import communication.StartCorner;
import communication.Transmission;

public class TestLocalization {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		UltrasonicSensor usRight = new UltrasonicSensor(SensorPort.S3);
		LightSensor rightLight = new LightSensor(SensorPort.S1);
		LightSensor leftLight = new LightSensor(SensorPort.S2);
		LightSampler rightSampler = new LightSampler(rightLight);
		LightSampler leftSampler = new LightSampler(leftLight);
		Transmission trans = new Transmission();
		trans.startingCorner = StartCorner.BOTTOM_LEFT;
		Decoder decoder = new Decoder(trans);
		decoder.decodeTranmission();
		Decoder.startCorner = StartCorner.BOTTOM_LEFT;
		//USLocalizer usLeft = new USLocalizer(patBot, us);
//		LocaliserNoBT localizer  = new LocaliserNoBT(patBot,usLeft, rightSampler, leftSampler);
		Localiser localizer  = new Localiser(patBot,usRight, rightSampler, leftSampler, decoder);

		new OdoLCD(odo);	
		Button.waitForAnyPress();
		rightSampler.startCorrectionTimer();
		leftSampler.startCorrectionTimer();

		localizer.dolocalise();
		Button.waitForAnyPress();
	}

}
