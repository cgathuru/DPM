package tests;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import navigation.Launcher;

public class LauncherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Button.waitForAnyPress();
		new Launcher();
		Launcher.drive(Motor.A, Motor.B, Motor.C);
		Button.waitForAnyPress();
		System.exit(0);

	}

}
