package pacman;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;


    public Direction myOpposite() {
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

    public boolean isOpposite(Direction dir){
        switch(this){
            case UP:
                if (dir == DOWN){
                    return  true;
                }
                break;
            case DOWN:
                if (dir == UP){
                    return  true;
                }
                break;
            case RIGHT:
                if (dir == LEFT){
                    return  true;
                }
                break;
            case LEFT:
                if (dir == RIGHT){
                    return  true;
                }
                break;
        }
        return false;
    }

    public int newRow(int currRow) {
        switch (this) {
            case UP:
                return currRow - 1;
            case DOWN:
                return currRow + 1;
            default:
                return currRow;
        }
    }

    public int newCol(int currCol) {
        switch (this) {
            case LEFT:
                return currCol - 1;
            case RIGHT:
                return currCol + 1;
            default:
                return currCol;
        }
    }

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
