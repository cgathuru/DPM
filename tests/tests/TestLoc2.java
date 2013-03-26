package tests;

import display.LCDInfo;
import navigation.Launcher;
import navigation.Navigation;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import robot.Odometer;
import robot.TwoWheeledRobot;
import sensors.LightLocalizer;
import sensors.USLocalizer;
import sensors.USLocalizerAway;
import sensors.USLocalizerFace;
import utilities.OdoLCD;

public class TestLoc2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		UltrasonicSensor usLeft = new UltrasonicSensor(SensorPort.S3);
		UltrasonicSensor usRight = new UltrasonicSensor(SensorPort.S4);
		LightSensor ls = new LightSensor(SensorPort.S1);
		USLocalizer us1 = new USLocalizer(patBot, usRight);
		USLocalizerFace usFace = new USLocalizerFace(patBot, usRight);
		USLocalizerAway usAway = new USLocalizerAway(patBot, usLeft);
		LightLocalizer lightLoc = new LightLocalizer(patBot, ls);
		Button.waitForAnyPress();
		new OdoLCD(odo);
		if (us1.returnDistance() > 60){
			usAway.doLocalization();
		}
		else{
			usFace.doLocalization();
		}
		//patBot.travelTo(30, 30);
		//patBot.turnTo(90);
		
		lightLoc.doLocalization();
		patBot.turnTo(0);
	//	new Launcher();
	//	Launcher.drive(Motor.A, Motor.B);
		patBot.motorFloat();
		patBot.travelTo(30, 30);
		//patBot.travelTo(0, 0);
		patBot.turnTo(0);
		Button.waitForAnyPress();
	}

}
