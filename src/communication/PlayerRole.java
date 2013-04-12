/**
* @author Sean Lawlor
* @date November 3, 2011
* @class ECSE 211 - Design Principle and Methods
*/
package communication;

/**
 * Defender: value 2, string "D"
 * Attacker: value 1, string "A"
 *
 */
public enum PlayerRole {
	/**
	 * The defender
	 */
	DEFENDER(2, "D"),
	/**
	 * The attacker
	 */
	ATTACKER(1, "A"),
	/**
	 * Nothing
	 */
	NULL(0, "");
	
	private int role;
	private String str;
	
	private PlayerRole(int rl, String str) {
		this.role = rl;
		this.str = str;
	}
	
	/**
	 * Converts the {@code PlayerRole} to a String
	 */
	public String toString() {
		return this.str;
	}
	
	/**
	 * Gets the ID of the role
	 * @return the value of the role
	 */
	public int getId() {
		return this.role;
	}
	
	/**
	 * Gets the role of the player given an ID
	 * @param rl The players ID
	 * @return The {@code PlayerRole} corresponding to the given ID 
	 */
	public static PlayerRole lookupRole(int rl) {
		for (PlayerRole role : PlayerRole.values())
			if (role.getId() == rl)
				return role;
		return PlayerRole.NULL;
	}
}
