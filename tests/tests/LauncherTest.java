package tests;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import navigaion.Launcher;

public class LauncherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Button.waitForAnyPress();
		new Launcher().drive(Motor.A, Motor.B);
		Button.waitForAnyPress();
		System.exit(0);

	}

}
