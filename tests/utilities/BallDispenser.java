package utilities;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class BallDispenser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TouchSensor touchUp = new TouchSensor(SensorPort.S1);
		TouchSensor touchDown = new TouchSensor(SensorPort.S2);
		
		while(true){
			if(touchUp.isPressed() || touchDown.isPressed()){
				Motor.A.rotateTo(180);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Motor.A.rotateTo(60);
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//end while
		
		

	}

}
