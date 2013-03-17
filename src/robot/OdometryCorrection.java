package robot;

import java.util.Stack;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.util.Timer;
import lejos.util.TimerListener;
import main.Constants;
import sensors.LightSampler;
/**
 * Corrects the {@link Odometer} periodically to make sure it stays accurate.
 *  Implements a {@code TimeListener} to periodically correct the {@link Odometer}
 * @author charles
 *
 */
public class OdometryCorrection implements TimerListener{

	private Odometer odometer;
	private TwoWheeledRobot robot;
	private LightSampler leftLs, rightLs;
	private NXTRegulatedMotor leftMotor;
	private Stack<Double> lineX, lineY, tachoCountX, tachoCountY;
	private Stack<SensorSide> sensorSideX, sensorSideY;
	/**
	 * The side the robot on which the sensor is located
	 * @author charles
	 *
	 */
	public enum SensorSide{
		/**
		 * The sensor is on the left side of the robot
		 */
		LEFT,
		/**
		 * The sensor is on the right side of the robot
		 */
		RIGHT};
	/**
	 * The line type detected. Either x or y.
	 * @author charles
	 *
	 */
	public enum LineType{
		/**
		 * The line detected is an x line 
		 * i.e. x = 30cm
		 */
		X, 
		/**
		 * The line detected is a y line 
		 * i.e. y = 30cm
		 */
		Y};
		
	private double x, y, theta, length;
	
	private long startTimeX, startTimeY, endTimeX, endTimeY;
	private long endTimeLight;
	
	/**
	 * The number of times the x value has been corrected on the {@link Odometer}.
	 */
	public static long xCor = 0;
	/**
	 * The number of times the y value has been corrected on the {@link Odometer}.
	 */
	public static long yCor = 0;
	/**
	 * The number of times the theta value has been corrected on the {@link Odometer}.
	 */
	public static int tCor = 0;
	
	public static double y1 = 0;
	public static double y2 = 0;
	/**
	 * The current light value reading from the {@link LightSensor} on the left side of the {@link TwoWheeledRobot}.
	 */
	public static int lsValue = Constants.DARK_LINE_VALUE;
	
	/**
	 * The current light value reading from the {@link LightSensor} on the right side of the {@link TwoWheeledRobot}.
	 */
	public static int rsValue = Constants.DARK_LINE_VALUE;
	
	
	private Timer correctionTimer;
	
	/**
	 * Initializes the class timer and the stacks used to store the {@link LineType}, {@link SensorSide}, and
	 * the value of the x or y line detected  
	 * @param odometer The {@link Odometer}
	 * @param ls1 The {@link LightSensor} on the left side of the robot
	 * @param ls2 The {@link LightSensor} on the right side of the robot
	 */
	public OdometryCorrection(TwoWheeledRobot robot, LightSensor ls1, LightSensor ls2){
		this.odometer = robot.getOdometer();
		this.robot = robot;
		this.leftLs = new LightSampler(ls1);
		this.rightLs = new LightSampler(ls2);
		this.leftMotor = Motor.A;
		lineX = new Stack<Double>();
		tachoCountX = new Stack<Double>();
		lineY = new Stack<Double>();
		tachoCountY = new Stack<Double>();
		sensorSideX = new Stack<SensorSide>();
		sensorSideY = new Stack<SensorSide>();
		correctionTimer = new Timer(Constants.ODOMETER_CORRECTION_TIMEOUT, this);
		//resetInternalTimer();
	}

	/**
	 * Corrects the {@link Odometer} periodically
	 */
	@Override
	public void timedOut() {
		/*
		 * If a line is detected, determine which line was detected.
		 * Store the line value, the tachometer reading and which sensor it was
		 * that detected the line.
		 * Start the respective line refresh counter
		 * If another line is detected determine if 
		 */
		if(System.currentTimeMillis() > endTimeX){ //if timer runs out flush memory for x values
			lineX.clear();
			tachoCountX.clear();
			sensorSideX.clear();
			resetInternalXTimer();
			
		}
		if(System.currentTimeMillis() > endTimeY){ //if timer runs out flush memory for y values
			lineY.clear();
			tachoCountY.clear();
			sensorSideY.clear();
			resetInternalYTimer();
			
		}
		if(!robot.isRotating()){
			//if left sensor detects a line
			if(leftLs.isDarkLine() && (System.currentTimeMillis() > endTimeLight)){
				Sound.beep();
				LineType lineType = determineLineType();
				//save the time
				//store the tacoCount
				double taco1 = leftMotor.getTachoCount();
				switch(lineType){
				case X:
					if(sensorSideX.empty()){ //if its the first time detecting the line add it to the stack
						sensorSideX.addElement(SensorSide.LEFT); //add the sensor which detected the line
						tachoCountX.push(new Double(taco1)); //save the tachometer count
						lineX.push(x); //save the value of the line
						resetInternalXTimer(); // start the x timer
					}
					else if(!isSameSensor(sensorSideX, SensorSide.LEFT) && isSameLine(lineType)){ //if its the second time and not the same sensor
						tachoCountX.push(new Double(taco1)); //store the tachometer value
						sensorSideX.addElement(SensorSide.LEFT); //add the sensor which detected the line
						double diff = Math.abs(tachoCountX.pop() - tachoCountX.pop()); //get the difference in tachometer readings
						calculateAndSetTheta(diff, SensorSide.LEFT); //calculate and set theta
						lineX.clear();
						tachoCountX.clear();
						sensorSideX.clear();
						resetInternalXTimer(); //reset the x timer
						
					}
					else{
						//do nothing because it was the same sensor
					}
					setX();
					break;
				case Y:
					if(sensorSideY.empty()){ //if its the first time detecting the line add it to the stack
						sensorSideY.addElement(SensorSide.LEFT); //add the sensor which detected the line
						tachoCountY.push(new Double(taco1)); //save the tachometer count
						lineY.push(y); //save the value of the line
						y1 = y;
						resetInternalYTimer(); // start the y timer
					}
					else if(!isSameSensor(sensorSideY, SensorSide.LEFT) && isSameLine(lineType)){ //if its the second time and not the same sensor
						tachoCountY.push(new Double(taco1)); //store the tachometer value
						sensorSideY.addElement(SensorSide.LEFT); //add the sensor which detected the line
						double diff = Math.abs(tachoCountY.pop() - tachoCountY.pop()); //get the difference in tachometer readings
						calculateAndSetTheta(diff, SensorSide.LEFT); //calculate and set theta
						y2 = y;
						lineY.clear();
						tachoCountY.clear();
						sensorSideY.clear();
						resetInternalYTimer(); //reset the y timer
						
					}
					else{
						//do nothing because it was the same sensor
					}
					setY();
					break;
				}
				
				
							
			}
			
			
			
			//if right sensor detects a line
			//if(isDarkLine(rsValue, this.rightLightValues)){
			//if(rsValue < Constants.DARK_LINE_VALUE){
			if(rightLs.isDarkLine() && (System.currentTimeMillis() > endTimeLight)){
				Sound.beep();
				LineType lineType = determineLineType();
				//save the time
				//store the tacoCount
				double taco1 = leftMotor.getTachoCount();
				switch(lineType){
				case X:
					if(sensorSideX.empty()){ //if its the first time detecting the line add it to the stack
						sensorSideX.addElement(SensorSide.RIGHT); //add the sensor which detected the line
						tachoCountX.push(new Double(taco1)); //save the tachometer count
						lineX.push(x); //save the value of the line
						resetInternalXTimer(); // start the x timer
					}
					else if(!isSameSensor(sensorSideX, SensorSide.RIGHT) && isSameLine(lineType)){ //if its the second time and not the same sensor
						tachoCountX.push(new Double(taco1)); //store the tachometer value
						sensorSideX.addElement(SensorSide.RIGHT); //add the sensor which detected the line
						double diff = Math.abs(tachoCountX.pop() - tachoCountX.pop()); //get the difference in tachometer readings
						calculateAndSetTheta(diff, SensorSide.RIGHT); //calculate and set theta
						lineX.clear();
						tachoCountX.clear();
						sensorSideX.clear();
						resetInternalXTimer(); //reset the x timer
						
					}
					else{
						//do nothing because it was the same sensor
					}
					setX();
					break;
				case Y:
					
					if(sensorSideY.empty()){ //if its the first time detecting the line add it to the stack
						sensorSideY.addElement(SensorSide.RIGHT); //add the sensor which detected the line
						tachoCountY.push(new Double(taco1)); //save the tachometer count
						lineY.push(y); //save the value of the line
						y1 =y;
						resetInternalYTimer(); // start the y timer
					}
					else if(!isSameSensor(sensorSideY, SensorSide.RIGHT) && isSameLine(lineType)){ //if its the second time and not the same sensor
					//else{
						tachoCountY.push(new Double(taco1)); //store the tachometer value
						sensorSideY.addElement(SensorSide.RIGHT); //add the sensor which detected the line
						double diff = Math.abs(tachoCountY.pop() - tachoCountY.pop()); //get the difference in tachometer readings
						calculateAndSetTheta(diff, SensorSide.RIGHT); //calculate and set theta
						y2 = y;
						lineY.clear();
						tachoCountY.clear();
						sensorSideY.clear();
						resetInternalYTimer(); //reset the y timer
						
					}
					else{
						//do nothing because it was the same sensor
					}
					setY();
					break;
				}
				
				
			}
		}
		
		lsValue = leftLs.getLightValue();
		rsValue = rightLs.getLightValue();
		
	}//timeout

	/**
	 * Resets the internal line refresh timer for x
	 */
	public void resetInternalXTimer() {
		startTimeX = System.currentTimeMillis();
		endTimeX = startTimeX + Constants.TIME_REFRESH_CONSTANT;
	}
	
	/**
	 * Resets the internal line refresh timer for y
	 */
	public void resetInternalYTimer() {
		startTimeY = System.currentTimeMillis();
		endTimeY = startTimeY + Constants.TIME_REFRESH_CONSTANT;
	}

	/**
	 * Checks if the same sensor had previously detected the currently detected line. As such
	 * it prevents angle correction from occurring prematurely.
	 * @param storedSensorSide A stack corresponding to either the x values or the y values
	 * @param sensorSide Either the left or the right light sensor
	 * @return If the same sensor detected the dark line
	 */
	public boolean isSameSensor(Stack<SensorSide> storedSensorSide, SensorSide sensorSide){
		if(storedSensorSide.empty()){
			return false;
		}
		return (storedSensorSide.peek().equals(sensorSide));
	}
	
	/**
	 * Determines whether the detected line value in the x direction or the y direction
	 * @return A {@code LineType} representing the line detected.
	 */
	public LineType determineLineType(){
		x = odometer.getX();
		y = odometer.getY();
		int xValue = (int)x;
		int yValue = (int)y;
		if((x + (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odometer.getTheta()))) % 30 < 2){
			xValue = (int) Math.abs(((x +  (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odometer.getTheta())))/30));
			xValue = xValue*30;
			x = xValue;
			return LineType.X;
		}

		yValue = (int) ((y +  (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.sin(Math.toRadians(odometer.getTheta())))/30);
		yValue = yValue*30;
		y = yValue;
		return LineType.Y;
	}


	/**
	 * Calculates theta based on the difference in the tachometer readings
	 * @param diff The difference in the tachometer readings
	 * @param sensorSide The last sensor to detect the line
	 */
	public void calculateAndSetTheta(double diff, SensorSide sensorSide) {
		double odoTheta = odometer.getTheta();
		double side = convertTacoToLength(diff);
		//calculate theta
		theta = Math.toDegrees(Math.atan(side/Constants.WIDTH));
		/*if(odoTheta > 270 && odoTheta < 360){
			theta = theta +360;
		}
		if(odoTheta >90 && odoTheta < 180 ){
			theta = theta + 180;
		}
		*/
		/*
		 * Left side means veering to the left and
		 * right side veering to the right
		 */
		if(odoTheta < 90){
			switch(sensorSide){
			case LEFT:
				theta = 90- theta;
				break;
			case RIGHT:
				
				break;
			}
		}
		else if(odoTheta == 90){
			switch(sensorSide){
			case LEFT:
				theta = 90- theta;
				break;
			case RIGHT:
				theta = theta +90;
				break;
			}
		}
		else if(odoTheta > 90 && odoTheta<180){
			switch(sensorSide){
			case LEFT:
				theta = 180- theta;
				break;
			case RIGHT:
				theta = theta +90;
				break;
			}
		}
		else if(odoTheta == 180){
			switch(sensorSide){
			case LEFT:
				theta = 180- theta;
				break;
			case RIGHT:
				theta = theta +180;
				break;
			}
		}
		else if(odoTheta > 180 && odoTheta < 270){
			switch(sensorSide){
			case LEFT:
				theta = 270- theta;
				break;
			case RIGHT:
				theta = theta + 180;
				break;
			}
		}
		else if(odoTheta == 270){
			switch(sensorSide){
			case LEFT:
				theta = 270- theta;
				break;
			case RIGHT:
				theta = theta + 270;
				break;
			}
		}
		else{ // if(odoTheta > 270 && odoTheta < 360)
			switch(sensorSide){
			case LEFT:
				theta = 360- theta;
				break;
			case RIGHT:
				theta = theta + 270;
				break;
			}
		}
		
		//set theta
		odometer.setTheta(theta);
		tCor++;
	}

	/**
	 * Converts the tachometer reading into a distance
	 * @param diff The difference in the tachometer count
	 * @return The distance moved
	 */
	public double convertTacoToLength(double diff) {
		length =  (diff/360)*2*Math.PI*Constants.WHEEL_RADIUS;
		return length;
	}

	/**
	 * Starts the odometry correction timer and the internal light samplers
	 */
	public void startCorrectionTimer(){
		endTimeLight = System.currentTimeMillis() + Constants.LIGHT_CALIBRATION_TIME; //set the amount of calibration time
		this.leftLs.startCorrectionTimer(); //start the left light sampler
		this.rightLs.startCorrectionTimer(); //start the right light sampler
		correctionTimer.start();
	}
	
	/**
	 * Stops the odometry correction timer and the internal light samplers
	 */
	public void stopCorrectionTimer(){
		this.leftLs.stopCorrectionTimer(); //stop the left light sampler
		this.rightLs.stopCorrectionTimer(); //stop the right light sampler
		correctionTimer.stop();		
	}
	
	/**
	 * Checks if the detected line is same line as previously detected line
	 * @param lineType Either an x line or a line
	 * @return True if the two lines are of the same
	 */
	public boolean isSameLine(LineType lineType){
		switch(lineType){
			case X:
				if(lineX.empty()){
					//tCor+=10;
					return false;
				}
				if(Math.abs(lineX.peek() - x) < Constants.MAX_LINE_CORRECTION_BANDWIDTH){
					Sound.buzz();
					return true;
				}
				return false;
			case Y:
				if(lineY.empty()){
					//tCor+=10;
					return false;
				}
				if( Math.abs(lineY.peek() - y) < Constants.MAX_LINE_CORRECTION_BANDWIDTH){
					Sound.buzz();
					return true;
				}
				return false;
		}
		return false;
	}
	
	/**
	 * Sets the x value of the {@link Odometer}
	 */
	public void setX(){
//		if(Math.abs(x % 30) < 1){
//			//correct the x value
//			x = ((int)(x/30))* 30;
//			xCor++;
//			double value = length*Math.sin(Math.toRadians(theta));
//			value = value/2;
//			odometer.setX( x);
//			//lineCheck(x);
//		}
		if( !sensorSideX.empty()){
			xCor++;
			if(sensorSideX.peek().equals(SensorSide.LEFT)){
				x= x - (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odometer.getTheta()));
				odometer.setX(x);
			}
			else{
				x= x + (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odometer.getTheta()));
				odometer.setX(x);;
			}
		}
		
	}
	
	/**
	 * Sets the y value of the {@link Odometer}
	 */
	public void setY(){
		
//		if(Math.abs(y % 30) < 1){
//			//correct the y value
//			y = ((int)(y/30))* 30;
//			yCor++;
//			double value = length*Math.sin(Math.toRadians(theta));
//			value = value/2;
//			odometer.setY(y);
//			//lineCheck(x);
//		}
		//if no sensor was detected a y line do nothing
		if(!sensorSideY.empty()){
			yCor++;
			if(sensorSideY.peek().equals(SensorSide.LEFT)){
				y= y- (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.sin(Math.toRadians(odometer.getTheta()));

			}
			else{
				y= y+ (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.sin(Math.toRadians(odometer.getTheta()));
			}
		}
		
		}
		
		
	}
	
