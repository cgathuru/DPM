package display;

import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * Controls the display of the screen on the NXT salve
 * @author charles
 *
 */
public class PrinterSlave extends Thread {
	
	
	private static final long DISPLAY_PERIOD = 250;
	
	/**
	 * Constructor
	 */
	public PrinterSlave() {

	}
	// run method (required for Thread).
	/**
	 * Checks periodically for new commands to execute from the master brick
	 */
	public void run() {
		long displayStart = 0, displayEnd;
		double[] position = new double[3];
		while (true) {
			LCD.clear();
			displayEnd = System.currentTimeMillis();
			if (displayEnd - displayStart < DISPLAY_PERIOD) {
				try {
					Thread.sleep(DISPLAY_PERIOD - (displayEnd - displayStart));
				} catch (InterruptedException e) {
					
				}
			}
			
						
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}
	
	//What is printed in the main menu.
	/**
	 * Prints the main menu of slave brick to its screen
	 */
	public static void printMainMenu() {
		LCD.clear();
		LCD.drawString("left = Navigator",  0, 0);
		LCD.drawString("right = Navigation2", 0, 1);
	}
	
}
