package utilities;

import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import robot.Odometer;
import sensors.LightSampler;

public class SamplerLCD implements TimerListener{
	private LightSampler leftLight, rightLight;
	public static final int LCD_REFRESH = 100;
	private Timer lcdTimer;

	public SamplerLCD(LightSampler leftLight, LightSampler rightLight){
		this.leftLight = leftLight;
		this.rightLight = rightLight;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		lcdTimer.start();
	}

	@Override
	public void timedOut() {
		// TODO Auto-generated method stub
		LCD.drawString("LeftAv", 0, 0);
		LCD.drawString("Leftv", 0, 2);
		LCD.drawString("RightAv", 0, 4);
		LCD.drawString("Rightv", 0, 6);
		LCD.drawInt(leftLight.getLightAverage(), 5, 0);
		LCD.drawInt(leftLight.getLightValue(), 5,2);
		LCD.drawInt(rightLight.getLightAverage(), 5, 4);
		LCD.drawInt(rightLight.getLightValue(), 5, 6);
	}
	
	
}
