package main;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import navigation.Defence;
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
import communication.Transmission;
import display.LCDInfo;

/**
 * Initialises all the components that dictate the robots behaviour and functionality.
 * @author charles
 *
 */
public class Initializer extends Thread{
	

	/**
	 * Starts 1 round of the competition.
	 */
	@Override
	public void run(){
		NXTRegulatedMotor leftMotor = Motor.A;
		NXTRegulatedMotor rightMotor = Motor.B;
		Odometer odo = new Odometer(false);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, leftMotor, rightMotor);
		UltrasonicSensor usLeft = new UltrasonicSensor(SensorPort.S3);
		UltrasonicSensor usRight = new UltrasonicSensor(SensorPort.S4);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		LightSampler leftLight = new LightSampler(lsLeft);
		LightSampler rightLight = new LightSampler(lsRight);
		Obstacle obstacle = new Obstacle(usLeft, usRight, odo);
		OdometryCorrection correction = new OdometryCorrection(patBot, leftLight, rightLight);
		Offence attack = new Offence(patBot, obstacle, correction);
		Defence defence =new Defence(patBot, obstacle, correction);
		LightLocalizer localizer= new LightLocalizer(patBot,usLeft, leftLight, rightLight);
		Button.waitForAnyPress();
		BluetoothConnection connection = new BluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		Transmission trans = connection.getTransmission();
		Decoder decoder = new Decoder(trans);
		decoder.decodeTranmission();
		new LCDInfo(odo);
		
		leftLight.startCorrectionTimer();
		rightLight.startCorrectionTimer();
		odo.startTimer();
		if(Decoder.playerRole.toString().equals("D")){
			defence.start();
		}
		else{
			attack.start();
		}
		Decoder.dispenserX = 90;
		Decoder.dispenserY = 30;
		localizer.dolocalise();
		correction.startCorrectionTimer();

		
		Button.waitForAnyPress();
	}
}
