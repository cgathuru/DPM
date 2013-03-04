package odometer;

import java.util.Stack;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Timer;
import lejos.util.TimerListener;
import main.Constants;

public class OdometryCorrection implements TimerListener{
	
	private Odometer odometer;
	private LightSensor leftLs;
	private LightSensor rightLs;
	private NXTRegulatedMotor leftMotor, rightMotor;
	private Stack<Double> line, tacoCount;
	private double x, y, theta, taco1, taco2;
	private boolean filter;
	
	public static int xCor = 0;
	public static int yCor = 0;
	
	private Timer correctionTimer;
	
	public OdometryCorrection(Odometer odometer, LightSensor ls1, LightSensor ls2, NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor){
		this.odometer = odometer;
		this.leftLs = ls1;
		this.rightLs = ls2;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		line = new Stack<Double>();
		tacoCount = new Stack<Double>();
		filter = true;
		correctionTimer = new Timer(Constants.ODOMETER_CORRECTION_TIMEOUT, this);
	}

	@Override
	public void timedOut() {

		//if left sensor detects a line
		if(leftLs.getLightValue() < Constants.DARK_LINE_VALUE){
			//store the tacoCount
			taco1 = leftMotor.getTachoCount();
			tacoCount.push(new Double(taco1));
			setOdometerValues();			
		}
		
		//if right sensor detects a line
		if(rightLs.getLightValue() < Constants.DARK_LINE_VALUE){
			//store the tacoCount
			taco2 = leftMotor.getTachoCount(); /*Use left motor to main consistency after obstacle avoidance*/
			tacoCount.push(new Double(taco2));
			setOdometerValues();
		}
		
	}//timeout

	private void setOdometerValues() {
		//get odometer values of x and y
		x = odometer.getX();
		y = odometer.getY();
		// check if the line is x or y
		if(Math.abs(x % 30) == 2){
			//correct the x value
			x = ((int)(x/30))* 30;
			xCor++;
			odometer.setX(x);
			lineCheck(x);
		}
		//if the line is y
		else{
			//correct the y value
			y = ((int)(y/30))* 30;
			yCor++;
			odometer.setY(y);
			lineCheck(y);
		}
	}

	/**
	 * Checks the line to determine if the same line has been crossed or not
	 * @param value
	 */
	private void lineCheck(double value) {
		//check if this is first time detecting the line
		if(line.isEmpty()){
			//if it is, add it too the stack
			line.push(new Double(value));
		}
		else{
			//check if it is the same line
			Double valueDouble = new Double(value);
			if(Math.abs(line.peek() - valueDouble) < Constants.MAX_LINE_CORRECTION_BANDWIDTH){
				//calculate the length of the adjacent side
				double side = valueDouble - line.pop();
				//calculate theta
				theta = Math.toDegrees(Math.atan(Constants.WIDTH/ side));
				//set theta
				odometer.setTheta(theta);
			}
			//if not the same line 
			else{
				//apply filter
				if(filter){
					//turn filter off for next time
					filter = false;
				}
				else{
					//clear the stack
					line.clear();
					//turn the filter back on
					filter = true;
				}
			}
		}
	}

	public void startCorrectionTimer(){
		correctionTimer.start();
	}
	
	

}
