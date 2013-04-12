/**
* @author Sean Lawlor, Stepan Salenikovich
* @date November 3, 2011
* @class ECSE 211 - Design Principle and Methods
*/
package communication;

/**
 * The corner at which th robot will start
 * BOTTOM LEFT X1 (0,0) "BL"
 * BOTTOM RIGHT X2 (10,0) "BR"
 * TOP RIGHT X3 (10,10) "TR"
 * TOP LEFT  X4 (0,10) "TL"
 */
public enum StartCorner {
/**
 * The bottom left corner of the playing field
 */
 BOTTOM_LEFT(1,0,0, "BL"),
 /**
  * The bottom right corner of the playing field
  */
 BOTTOM_RIGHT(2,10,0, "BR"),
 /**
  * The top right corner of the playing field
  */
 TOP_RIGHT(3,10,10, "TR"),
 /**
  * The top left corner of the playing field
  */
 TOP_LEFT(4,0,10, "TL"),
 /**
  * The default (bottom left) corner of the playing field
  */
 NULL(0,0,0, "NULL");
 
 private int id, x, y;
 private String name;
 private StartCorner(int id, int x, int y, String name) {
  this.id = id;
  this.x = x;
  this.y = y;
  this.name = name;
 }
 
 /**
  * Converts the starting corner into a String
  */
 public String toString() {
  return this.name;
 }
 
 /**
  * Gets the coordinates of the starting corner
  * @return The coordinates of the starting corner
  */
 public int[] getCooridinates() {
  return new int[] {this.x, this.y};
 }
 
 /**
  * Gets the starting corners x position
  * @return The starting corners x position
  */
 public int getX() {
  return this.x;
 }
 
 /**
  * Gets the starting corners y position
  * @return The starting corners y position
  */
 public int getY() {
  return this.y;
 }
 
 /**
  * Gets the I.D. corresponding to the robots starting corner
  * @return
  */
 public int getId() {
  return this.id;
 }
 
 /**
  * Looks for the starting corner based on the corner ID of the robot
  * @param cornerId The {@code cornerId} of the starting corner
  * @return The starting corner corresponding to the give {@code cornerId}
  */
 public static StartCorner lookupCorner(int cornerId) {
  for (StartCorner corner : StartCorner.values())
   if (corner.id == cornerId)
    return corner;
  return NULL;
 }
}
