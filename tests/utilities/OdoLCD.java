package utilities;

import lejos.nxt.LCD;
import robot.Odometer;
import robot.OdometryCorrection;
import display.LCDInfo;

public class OdoLCD extends LCDInfo{
	

	public OdoLCD(Odometer odo) {
		super(odo);
		
	}
	
	@Override
	public void timedOut(){
		super.timedOut();
		LCD.drawString("Ls: ", 0, 3);
		LCD.drawString("Rs: ", 0, 4);
		LCD.drawString("LType: ", 5, 0);
		LCD.drawString("RType: ", 5, 1);
		LCD.drawString(OdometryCorrection.left , 12, 0);
		LCD.drawString(OdometryCorrection.right, 12, 1);
		LCD.drawInt(OdometryCorrection.lsValue, 5, 3);
		LCD.drawInt(OdometryCorrection.rsValue, 5, 4);
		LCD.drawString("XCor: ", 0, 5);
		LCD.drawString("YCor: ", 0, 6);
		LCD.drawString("TCor: ", 0, 7);
		LCD.drawString("" + OdometryCorrection.xCor, 5, 5);
		LCD.drawString("" + OdometryCorrection.yCor, 5, 6);
		LCD.drawInt(OdometryCorrection.tCor, 5, 7);
		LCD.drawString("Y1: ", 7, 5);
		LCD.drawString("Y2: ", 7, 6);
		LCD.drawInt((int)OdometryCorrection.y1, 10, 5);
		LCD.drawInt((int)OdometryCorrection.y2, 10, 6);
		
	}

}
