/**
* @author Sean Lawlor, Stepan Salenikovich, Charles
* 
*/
package communication;

import main.Constants;

/**
 * The transmission information to be received when information is received via bluetooth
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
	 * which is then stored in the {@link Decoder} 
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
