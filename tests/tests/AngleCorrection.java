package tests;

import navigaion.Navigation;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import odometer.Odometer;
import odometer.OdometryCorrection;
import odometer.TwoWheeledRobot;
import utilities.OdoLCD;

public class AngleCorrection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
		Navigation nav = odo.getNavigation();
		LightSensor lsLeft = new LightSensor(SensorPort.S1);
		LightSensor lsRight = new LightSensor(SensorPort.S2);
		new OdoLCD(odo, lsLeft, lsRight);
		OdometryCorrection odoCorrect = new OdometryCorrection(odo, lsLeft, lsRight);
		Button.waitForAnyPress();
		odoCorrect.startCorrectionTimer();
		patBot.moveForwardBy(60);
		//nav.turnTo(180);
		//.moveForwardBy(60);
		nav.turnTo(0);
		Button.waitForAnyPress();
		System.exit(0);
		

	}

}
