/*this interface will the implemented by ghosts, dots, and energizers*/

package Pacman;

public interface Collidable {

    public int[] getCoordinates();
    public void executeCollision();
    public int getScore();
    public SquareType getType();
    public void setLocation(int x, int y);

}
