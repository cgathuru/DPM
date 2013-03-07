package utilities;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import main.Constants;
import odometer.Odometer;
import odometer.OdometryCorrection;
import display.LCDInfo;

public class OdoLCD extends LCDInfo{
	
	private LightSensor leftLs, rightLs;

	public OdoLCD(Odometer odo, LightSensor leftLs, LightSensor rightLs) {
		super(odo);
		this.leftLs = leftLs;
		this.rightLs = rightLs;
		
	}
	
	@Override
	public void timedOut(){
		super.timedOut();
		LCD.drawString("Ls: ", 0, 3);
		LCD.drawString("Rs: ", 0, 4);
		LCD.drawInt(OdometryCorrection.lsValue, 5, 3);
		LCD.drawInt(OdometryCorrection.rsValue, 5, 4);
		LCD.drawString("XCor: ", 0, 5);
		LCD.drawString("YCor: ", 0, 6);
		LCD.drawInt(OdometryCorrection.xCor, 5, 5);
		LCD.drawInt(OdometryCorrection.yCor, 5, 6);
		
	}

}
