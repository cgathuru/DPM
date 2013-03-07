package utilities;

import odometer.Odometer;
import display.LCDInfo;

public class NavLCD extends LCDInfo{

	public NavLCD(Odometer odo) {
		super(odo);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void timedOut(){
		super.timedOut();
		
	}

}
