package display;

import odometer.Odometer;
import odometer.OdometryCorrection;
import sensors.USLocalizer;
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
	private USLocalizer usl;
	
	// arrays for displaying data
	private double [] pos;
	/**
	 * Initializes all variables in the class
	 * @param odo
	 */
	public LCDInfo(Odometer odo) {
		this.odo = odo;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		this.usl = usl;
		
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
		/*LCD.drawString("Angle A: ", 0, 3);
		LCD.drawString("Angle B: ", 0, 4);
		LCD.drawInt(usl.getAngle1(), 9, 3);
		LCD.drawInt(usl.getAngle2(), 9, 4);
		LCD.drawString("XCor: ", 0, 5);
		LCD.drawString("YCor: ", 0, 6);
		LCD.drawInt(OdometryCorrection.xCor, 5, 5);
		LCD.drawInt(OdometryCorrection.yCor, 5, 6);
		*/
	}
}
