package tests;

import communication.BluetoothConnection;
import communication.Decoder;
import communication.PlayerRole;
import communication.StartCorner;
import communication.Transmission;

import main.Constants;
import navigation.Defence;
import navigation.Launcher;
import navigation.Obstacle;
import navigation.Offence;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
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
				decoder.defenceX = Constants.TILE_DISTANCE_TRUNCATED * Constants.GOALX;
				decoder.defenceY = Constants.TILE_DISTANCE_TRUNCATED * (Constants.GOALY - 2);
				Defence defence = new Defence(patBot, obstacle, correction, decoder);

				Localiser localizer= new Localiser(patBot,usLeft, leftLight, rightLight, decoder);
				new OdoLCD(odo);
				Button.waitForAnyPress();
				
				leftLight.startCorrectionTimer();
				rightLight.startCorrectionTimer();
				odo.startTimer();
				Decoder.dispenserX = 90;
				Decoder.dispenserY = 30;
				localizer.dolocalise();
				correction.startCorrectionTimer();
				defence.start();
				Button.waitForAnyPress();
	}

}
