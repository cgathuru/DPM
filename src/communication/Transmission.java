/*
* @author Sean Lawlor
* @date November 3, 2011
* @class ECSE 211 - Design Principle and Methods
*/
package communication;

/*
 * Skeleton class to hold datatypes needed for final project
 * 
 * Simply all public variables so can be accessed with 
 * Transmission t = new Transmission();
 * int d1 = t.d1;
 * 
 * and so on...
 * 
 * Also the role is an enum, converted from the char transmitted. (It should never be
 * Role.NULL)
 */

/**
 * Contains all the data that will be sent during the bluetooth connection
 * @author charles
 *
 */
public class Transmission {
	
	/**
	 * The corner type. An {@code enum} is used because there can only be 4 corners
	 */
	public static enum CORNER_LOCATION{ TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT};
	/**
	 * The x position of the location of the goal
	 */
	public static int goalX;
	/**
	 * The y position of the location of the goal
	 */
	public static int goalY;
	
	/**
	 * the corner the robot is starting in
	 */
	public static CORNER_LOCATION cornerLocation;
	
	/**
	 * The x position of the location of the ball dispenser
	 */
	public static int ballDispenserX;
	
	/**
	 * The y position of the location of the ball dispenser
	 */
	public static int ballDispenserY;
	
	
	
}
