package display;

import odometer.Odometer;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;

/**
 * Controls the display screen of the  master brick.
 * @author charles
 *
 */
public class LCDInfo implements TimerListener{
	public static final int LCD_REFRESH = 100;
	private Odometer odo;
	private Timer lcdTimer;
	
	// arrays for displaying data
	private double [] pos;
	/**
	 * Initializes all variables in the class
	 * @param odo
	 */
	public LCDInfo(Odometer odo) {
		this.odo = odo;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		
		// initialise the arrays for displaying data
		pos = new double [3];
		
		// start the timer
		lcdTimer.start();
	}
	/**
	 * Displays the information to the screen after a timeout
	 */
	public void timedOut() { 
		odo.getPosition(pos);
		LCD.clear();
		LCD.drawString("X: ", 0, 0);
		LCD.drawString("Y: ", 0, 1);
		LCD.drawString("H: ", 0, 2);
		LCD.drawInt((int)(pos[0] * 10), 3, 0);
		LCD.drawInt((int)(pos[1] * 10), 3, 1);
		LCD.drawInt((int)pos[2], 3, 2);
	}
}
