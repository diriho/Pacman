/*this interface will the implemented by ghosts, dots, and energizers*/

package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;

public interface Collidable {

    public int[] getCoordinates();
    public void executeCollision();
    public int getScore();
    public CS15SquareType getType();

}
