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
	
	/**
	 * The value of the first line detected
	 */
	public static double y1 = 0;
	/**
	 * The value of the next line detected
	 */
	public static double y2 = 0;
	
	/**
	 * The {@link LineType} detected by the left light sensor
	 */
	public static String left= "";
	/**
	 * The {@link LineType} detected by the right light sensor
	 */
	public static String right = "";
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
	 * @param ls1 The {@link LightSampler} associated with the left side of the robot
	 * @param ls2 The {@link LightSampler} associated with the right side of the robot
	 */
	public OdometryCorrection(TwoWheeledRobot robot, LightSampler ls1, LightSampler ls2){
		this.odometer = robot.getOdometer();
		this.robot = robot;
		this.leftLs = ls1;
		this.rightLs = ls2;
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
			clearStoredXValues();
			resetInternalXTimer();
			
		}
		if(System.currentTimeMillis() > endTimeY){ //if timer runs out flush memory for y values
			clearStoredYValues();
			resetInternalYTimer();
			
		}
		if(!robot.isRotating()){
			//if left sensor detects a line
			if(leftLs.isDarkLine() && (System.currentTimeMillis() > endTimeLight)){
				Sound.beep();
				LineType lineType = determineLineType(SensorSide.LEFT);
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
						Sound.buzz();
						tachoCountX.push(new Double(taco1)); //store the tachometer value
						sensorSideX.addElement(SensorSide.LEFT); //add the sensor which detected the line
						double diff = Math.abs(tachoCountX.pop() - tachoCountX.pop()); //get the difference in tachometer readings
						calculateAndSetTheta(diff, SensorSide.LEFT); //calculate and set theta
						clearStoredXValues();
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
						Sound.buzz();
						tachoCountY.push(new Double(taco1)); //store the tachometer value
						sensorSideY.addElement(SensorSide.LEFT); //add the sensor which detected the line
						double diff = Math.abs(tachoCountY.pop() - tachoCountY.pop()); //get the difference in tachometer readings
						calculateAndSetTheta(diff, SensorSide.LEFT); //calculate and set theta
						y2 = y;
						clearStoredYValues();
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
				LineType lineType = determineLineType(SensorSide.RIGHT);
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
						Sound.buzz();
						tachoCountX.push(new Double(taco1)); //store the tachometer value
						sensorSideX.addElement(SensorSide.RIGHT); //add the sensor which detected the line
						double diff = Math.abs(tachoCountX.pop() - tachoCountX.pop()); //get the difference in tachometer readings
						calculateAndSetTheta(diff, SensorSide.RIGHT); //calculate and set theta
						clearStoredXValues();
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
						Sound.buzz();
						tachoCountY.push(new Double(taco1)); //store the tachometer value
						sensorSideY.addElement(SensorSide.RIGHT); //add the sensor which detected the line
						double diff = Math.abs(tachoCountY.pop() - tachoCountY.pop()); //get the difference in tachometer readings
						calculateAndSetTheta(diff, SensorSide.RIGHT); //calculate and set theta
						y2 = y;
						clearStoredYValues();
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
		else{ //if robot is rotating
			clearStoredValues();
			resetInternalTimers();
		}
		
		/*Get the light values for display purposes*/
		lsValue = leftLs.getLightValue();
		rsValue = rightLs.getLightValue();
		
	}//timeout

	/**
	 * Clears out the values stored when a {@link LineType} of y is detected
	 */
	public void clearStoredYValues() {
		lineY.clear();
		tachoCountY.clear();
		sensorSideY.clear();
	}


	/**
	 * Clears out the values stored when a {@link LineType} of x is detected
	 */
	public void clearStoredXValues() {
		lineX.clear();
		tachoCountX.clear();
		sensorSideX.clear();
	}

	/**
	 * Clears any stored x and y values for angle correction
	 */
	public void clearStoredValues(){
		clearStoredXValues();
		clearStoredYValues();
	}
	
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
	 * Resets the internal x and y line refresh timers
	 */
	public void resetInternalTimers(){
		resetInternalXTimer();
		resetInternalYTimer();
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
	public LineType determineLineType(SensorSide sensorSide){
		x = odometer.getX();
		y = odometer.getY();
		double xValue = (int)x;
		double yValue = (int)y;
		if((x + (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odometer.getTheta()))) % 30 < 1){
			xValue = (int) Math.abs(((x +  (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odometer.getTheta())))/30));
			xValue = xValue*Constants.TILE_DISTANCE;
			x = xValue;
			switch(sensorSide){
			case LEFT:
				left = "x";
				break;
			case RIGHT:
				right = "x";
				break;
			}
			return LineType.X;
		}

		yValue = (int) ((y +  (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.sin(Math.toRadians(odometer.getTheta())))/30);
		yValue = yValue*Constants.TILE_DISTANCE;
		y = yValue;
		switch(sensorSide){
		case LEFT:
			left = "y";
			break;
		case RIGHT:
			right = "y";
			break;
		}
		return LineType.Y;
	}

	/**
	 * Checks if the current line detected is an x line
	 * @param sensorSide The {@code SesnorSide} of the light sensor that detected the dark line
	 * @return
	 */
	public boolean isLineX(SensorSide sensorSide) {
		//return (x + (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odometer.getTheta()))) % 30 < 2;
		double odoTheta = odometer.getTheta();
		double odoX = odometer.getX();
		int xValue = (int)x;
		if(odoTheta == 0){
			return false;
		}
		else if(odoTheta == 45){
			switch(sensorSide){
			case LEFT:
				//subtract because left sensor will be 'behind' the robot to get to line
				if ((odoX - (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) % 30 < 1){
					xValue = (int) ((odoX - (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) / 30);
					return true;
				}
			case RIGHT:
				//add because right sensor will be 'ahead' of the robot to get to line
				return (odoX + (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) % 30 < 1;
			}
		}
		else if(odoTheta < 90 && odoTheta > 0){
			//Sound.beepSequence();
			switch(sensorSide){
			case LEFT:
				//subtract because left sensor will be 'behind' the robot to get to line
				if ((odoX - (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) % 30 < 1){
					xValue = (int) ((odoX - (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) / 30);
					return true;
				}
			case RIGHT:
				//add because right sensor will be 'ahead' of the robot to get to line
				return (odoX + (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) % 30 < 1;
			}
		}
		else if(odoTheta == 90){
			return true;
		}
		else if(odoTheta > 90 && odoTheta<180){
			switch(sensorSide){
			case LEFT:
				//subtract because left sensor will be 'behind' the robot to get to line (theta negative)
				return (odoX + (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) % 30 < 1;
			case RIGHT:
				//add because right sensor will be 'ahead' of the robot to get to line (theta negative)
				return (odoX - (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) % 30 < 1;
			}
		}
		else if(odoTheta == 180){
			return false;
		}
		else if(odoTheta > 180 && odoTheta < 270){
			switch(sensorSide){
			case LEFT:
				//subtract because left sensor will be 'behind' the robot to get to line (theta negative)
				return (odoX + (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) % 30 < 1;
			case RIGHT:
				//add because right sensor will be 'ahead' of the robot to get to line (theta negative)
				return (odoX - (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) % 30 < 1;
			}
		}
		else if(odoTheta == 270){
			return true;
		}
		else{ // if(odoTheta > 270 && odoTheta < 360)
			switch(sensorSide){
			case LEFT:
				//add because left sensor will be 'ahead' of the robot to get to line
				return (odoX + (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) % 30 < 1;
			case RIGHT:
				//subtract because right sensor will be 'behind' of the robot to get to line
				return (odoX - (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odoTheta))) % 30 < 1;
			}
		}
		return false;
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
		theta = Math.abs(Math.toDegrees(Math.atan(side/Constants.WIDTH)));
		/*if(odoTheta > 270 && odoTheta < 360){
			theta = theta +360;
		}
		if(odoTheta >90 && odoTheta < 180 ){
			theta = theta + 180;
		}
		*/
		if(odoTheta % 45 < 5){
			theta = odoTheta;
			return;
		}
		/*
		 * Left side means veering to the left and
		 * right side veering to the right
		 */
		if(odoTheta == 0){
			//Sound.buzz();
			switch(sensorSide){
			case LEFT:
				theta = 360 - theta;
				break;
			case RIGHT:
				
				break;
			}
		}
		else if(odoTheta < 90 && odoTheta > 0){
			//Sound.beepSequence();
			switch(sensorSide){
			case LEFT:
				theta = 360 - theta;
				break;
			case RIGHT:
				
				break;
			}
		}
		else if(odoTheta == 90){
			//Sound.beepSequenceUp();
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
				theta = 180 - theta;
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
		if(isThetaValid(theta)){
			odometer.setTheta(theta);
			tCor++;
		}
		
	}
	
	/**
	 * Checks if the calculated value of theta makes sense. If so, theta is corrected.
	 * @param theta The calculated value of theta that the robots heading should be corrected to
	 * @return
	 */
	public boolean isThetaValid(double theta){
		double odoTheta = odometer.getTheta();
		if(Math.abs(odoTheta - theta) <15){
			return true;
		}
		if(odoTheta == 0 && theta > 10){
			if(360 - theta < 10){
				return true;
			}
			return false;
		}
		if(theta % odoTheta > 5){
			return false;
		}
		return false;
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
		this.leftLs.startCorrectionTimer(); //start the left light sampler
		this.rightLs.startCorrectionTimer(); //start the right light sampler
		this.correctionTimer.start();
	}
	
	/**
	 * Stops the odometry correction timer and the internal light samplers
	 */
	public void stopCorrectionTimer(){
		this.leftLs.stopCorrectionTimer(); //stop the left light sampler
		this.rightLs.stopCorrectionTimer(); //stop the right light sampler
		this.correctionTimer.stop();		
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
					return true;
				}
				return false;
			case Y:
				if(lineY.empty()){
					//tCor+=10;
					return false;
				}
				if( Math.abs(lineY.peek() - y) < Constants.MAX_LINE_CORRECTION_BANDWIDTH){
					return true;
				}
				return false;
		}
		return false;
	}//isSameLine
	
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
		if( !sensorSideX.empty() && (odometer.getX() % 30 < 5) &&  (odometer.getTheta() % 45 < 3) ){
			xCor++;
			if(sensorSideX.peek().equals(SensorSide.LEFT)){
				x= x - (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odometer.getTheta()));
				if(isValidX())
				odometer.setX(x);
			}
			else{
				x= x + (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.cos(Math.toRadians(odometer.getTheta()));
				if(isValidX())
				odometer.setX(x);;
			}
		}
		
	}//setX
	
	/**
	 * Checks if the corrected value of x makes sense.
	 * @return I the corrected x value makes sense
	 */
	public boolean isValidX(){
		return Math.abs(x - odometer.getX()) < 3;
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
		if(!sensorSideY.empty() && (odometer.getY() % 30 < 3) && (odometer.getTheta() % 45 < 3)){
			yCor++;
			if(sensorSideY.peek().equals(SensorSide.LEFT)){
				y= y- (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.sin(Math.toRadians(odometer.getTheta()));
				if(isValidY())
				odometer.setY(y);
			}
			else{
				y= y+ (Constants.ODOMETRY_CORRECTION_MAX_ERROR_ALLOWANCE)* Math.sin(Math.toRadians(odometer.getTheta()));
				if(isValidY())
				odometer.setY(y);
			}
		}
		
	}//setY
	
	/**
	 * Checks if the corrected value of y makes sense
	 * @return If the corrected value of y makes sense
	 */
	public boolean isValidY(){
		return Math.abs(y - odometer.getY()) < 3;
	}
	
	/**
	 * Gets the left {@code LightSampler} used by {@code OdometerCorrection}
	 * @return The left {@code LightSampler} used by the robot for {@code OdometeryCorrection}
	 */
	public LightSampler getLeftLightSampler(){
		return this.leftLs;
	}
	
	/**
	 * Gets the right {@code LightSampler} used by {@code OdometerCorrection}
	 * @return The right {@code LightSampler} used by the robot for {@code OdometeryCorrection}
	 */
	public LightSampler getRightLightsampler(){
		return this.rightLs;
	}
	
	/**
	 * Stops {@code OdometryCorrection} and the {@code LightSampler} for each light sensor used for {@code OdometryCorrection}
	 * @param lightSamplers True if the the {@code LightSampler} for each sensor should be stopped
	 */
	public void stopCorrectionTimer(boolean lightSamplers){
		this.correctionTimer.stop();
		if(lightSamplers){
			leftLs.stopCorrectionTimer();
			rightLs.stopCorrectionTimer();
		}
		else{
			stopCorrectionTimer();
		}
	}
	
	/**
	 * Stops the correction timer and a {@code LightSampler} that is not on the specified sensor side
	 * @param sensorSide The {@link SensorSide} of the {@link LightSampler} that should not be stopped
	 */
	public void stopCorrectionTimer(SensorSide sensorSide){
		this.correctionTimer.stop();
		switch(sensorSide){
		case LEFT:
			rightLs.stopCorrectionTimer();
			break;
		case RIGHT:
			leftLs.stopCorrectionTimer();
			break;
		}
	}
}//end OdometryCorrection
	
