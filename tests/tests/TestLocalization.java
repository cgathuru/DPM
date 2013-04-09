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
import utilities.SamplerLCD;

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
		UltrasonicSensor usLeft = new UltrasonicSensor(SensorPort.S3);
		UltrasonicSensor usRight = new UltrasonicSensor(SensorPort.S4);
		LightSensor leftLight = new LightSensor(SensorPort.S1);
		LightSensor rightLight = new LightSensor(SensorPort.S2);
		LightSampler rightSampler = new LightSampler(rightLight);
		LightSampler leftSampler = new LightSampler(leftLight);
		Transmission trans = new Transmission();
		trans.decodeTranmission();
		trans.startingCorner = StartCorner.BOTTOM_LEFT;
		Decoder.startCorner = StartCorner.BOTTOM_LEFT;
		//USLocalizer usLeft = new USLocalizer(patBot, us);
//		LocaliserNoBT localizer  = new LocaliserNoBT(patBot,usLeft, rightSampler, leftSampler);
		Localiser localizer  = new Localiser(patBot,usLeft, leftSampler, rightSampler);

		//new OdoLCD(odo);	
		new SamplerLCD(leftSampler, rightSampler);
		Button.waitForAnyPress();
		rightSampler.startCorrectionTimer();
		leftSampler.startCorrectionTimer();
		localizer.dolocalise();
	
	
	}

}
