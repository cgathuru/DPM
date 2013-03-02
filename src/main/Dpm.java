package main;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import odometer.Odometer;
import odometer.TwoWheeledRobot;
import sensors.LightLocalizer;
import sensors.USLocalizer;

import communication.Instructions;
import communication.NXTSend;

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
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
		LCDInfo lcd = new LCDInfo(odo);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		LightSensor ls = new LightSensor(SensorPort.S1);
		do {
			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		// perform the ultrasonic localization
		USLocalizer usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.FALLING_EDGE);
		usl.doLocalization();
		/*Navigation nav = odo.getNavigation();
		nav.turnTo(0);
		nav.turnTo(90);
		nav.turnTo(180);
		nav.turnTo(270);
		nav.turnTo(360);
		*/
		// perform the light sensor localization
		//LightLocalizer lsl = new LightLocalizer(odo, ls);
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
