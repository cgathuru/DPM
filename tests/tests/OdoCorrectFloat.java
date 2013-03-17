package tests;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import robot.Odometer;
import robot.OdometryCorrection;
import robot.TwoWheeledRobot;
import utilities.OdoLCD;

public class OdoCorrectFloat {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int buttonChoice;
		Odometer odo = new Odometer(true);
		TwoWheeledRobot patBot = new TwoWheeledRobot(odo, Motor.A, Motor.B);
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		new OdoLCD(odo);
		OdometryCorrection odoCorrect = new OdometryCorrection(patBot, lsLeft, lsRight);
		do {
			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		odoCorrect.startCorrectionTimer();
		Motor.A.flt();
		Motor.B.flt();
		
		Button.waitForAnyPress();
		System.exit(0);

	}

}
