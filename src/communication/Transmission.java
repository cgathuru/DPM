/**
* @author Sean Lawlor, Stepan Salenikovich
* @date November 3, 2011
* @class ECSE 211 - Design Principle and Methods
*/
package communication;

import main.Constants;

/**
 * Skeleton class to hold datatypes needed for final project
 * 
 * Simply all public variables so can be accessed with 
 * Transmission t = new Transmission();
 * int fx = t.fx;
 * 
 * and so on...
 * 
 * Also the role is an enum, converted from the char transmitted. (It should never be
 * Role.NULL)
 */

public class Transmission {
	/**
	 * The role, Defender or Attacker
	 */
	public PlayerRole role;
	/**
	 * Ball dispenser X tile position
	 */
	public int bx;
	/**
	 * Ball dispenser Y tile position
	 */
	public int by;
	/**
	 * Defender zone dimension 1
	 */
	public int w1;
	/**
	 * Defender zone dimension 2
	 */
	public int w2;
	/**
	 * Forward line distance from goal
	 */
	public int d1;
	/**
	 * starting corner, 1 through 4
	 */
	public StartCorner startingCorner;
	
	
	/**
	 * Translates the data in the {@link Transmission} into usable information
	 */
	public void decodeTranmission(){
		Decoder.goalX = Constants.GOALX * Constants.TILE_DISTANCE_TRUNCATED;
		Decoder.goalY = Constants.GOALY * Constants.TILE_DISTANCE_TRUNCATED;
		Decoder.processAttack();
		Decoder.processDefence(w2);
		Decoder.dispenserX = bx * Constants.TILE_DISTANCE_TRUNCATED;
		Decoder.dispenserY = by * Constants.TILE_DISTANCE_TRUNCATED;
		Decoder.startCorner = startingCorner;
		Decoder.playerRole = role;
				
		
	}
}
