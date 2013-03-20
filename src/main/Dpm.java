package main;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import navigation.Navigation;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import sensors.USLocalizer;

import communication.BluetoothConnection;

import display.LCDInfo;

/**
 * Contains the main method of the master brick. Initializes all the sensor 
 * components and motors attached to the master brick.
 * @author charles
 *
 */
public class Dpm {

	public static void main(String[] args) {
		int buttonChoice;
		// setup the odometer, display, and ultrasonic and light sensors		
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		USLocalizer usl = new USLocalizer(patBot, us);
		//Bluetooth connection with Mufasa (our robot)
		BluetoothConnection connection = new BluetoothConnection();
		LCD.clear();
		connection.printTransmission();
		LCDInfo lcd = new LCDInfo(odo);
		/*
		do {
			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		// perform the ultrasonic localization
		
		//usl.doLocalization();
		 */
		Navigation nav = new Navigation(patBot);
		OdometryCorrection odometertyCorrection = new OdometryCorrection(patBot, lsLeft, lsRight);
		//nav.travelTo(0, 60);
		//nav.moveForwardBy(60);
		odometertyCorrection.startCorrectionTimer();
		//nav.travelTo(60, 0);
		//nav.travelTo(0, 0);
		//nav.turnTo(0);
		/*nav.turnToImmediate(90);
		nav.moveForwardBy(60);
		nav.turnToImmediate(90);
		nav.moveForwardBy(60);
		nav.turnToImmediate(90);
		nav.moveForwardBy(60);
		nav.turnToImmediate(90);
	*/
		/*nav.turnTo(0);
		nav.turnTo(90);
		nav.turnTo(180);
		nav.turnTo(270);
		nav.turnTo(360);
		*/
		// perform the light sensor localization
		//LightLocalizer lsl = new LightLocalizer(odo, ls1);
		//lsl.doLocalization();
		/*try {
			NXTSend.send(Instructions.SHOOT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		Button.waitForAnyPress();
		System.exit(0);
		/*try {
			NXTSend.send(Instructions.CLOSE_CONNECTION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NXTSend.closeConnection();
		*/
	}

}
