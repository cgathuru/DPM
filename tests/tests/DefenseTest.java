package tests;

import communication.BluetoothConnection;
import communication.Decoder;
import communication.PlayerRole;
import communication.StartCorner;
import communication.Transmission;
import display.LCDInfo;

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
				/*Decoder decoder = new Decoder(new Transmission());
				Decoder.startCorner = StartCorner.BOTTOM_LEFT;
				Decoder.defenceX = Constants.TILE_DISTANCE_TRUNCATED * Constants.GOALX;
				Decoder.defenceY = Constants.TILE_DISTANCE_TRUNCATED * (Constants.GOALY - 2);*/
				
				Button.waitForAnyPress();
				//BluetoothConnection connection = new BluetoothConnection();
				//LCD.clear();
				//connection.printTransmission();
				new LCDInfo(odo);
				//Transmission trans = connection.getTransmission();
				//trans.decodeTranmission();
				Defence defence = new Defence(patBot, obstacle, correction);

				
				leftLight.startCorrectionTimer();
				rightLight.startCorrectionTimer();
				odo.startTimer();
				Transmission trans = new Transmission();
				Decoder.startCorner = StartCorner.TOP_RIGHT;
				Decoder.dispenserX = 90;
				Decoder.dispenserY = 30;
				Decoder.defenceX = 150;
				Decoder.defenceY = 240;
				Decoder.shootX = 150;
				Decoder.shootY = 60;
				Localiser localizer= new Localiser(patBot,usLeft, leftLight, rightLight);
				localizer.dolocalise();
				correction.startCorrectionTimer();
				defence.start();
				Button.waitForAnyPress();
	}

}
