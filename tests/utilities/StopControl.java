package utilities;

import lejos.nxt.Button;

public class StopControl extends Thread{
	
	@Override
	public void run(){
		Button.waitForAnyPress();
		System.exit(0);
	}
}
