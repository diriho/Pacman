/*this Direction Enum class models all the different directions that pacman and ghost
* will have at different stages of the game
* */

package pacman;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;


    /**this helper method returns the opposite of each of the directions**/
    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            default:
                return LEFT;
        }
    }

}
