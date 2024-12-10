/**this class models the properties and functionalities of pacman with all the necessar encapsulations
 * It extends the Dot class, but also it implements the collidable interface**/


package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Pacman extends Dot{

    private Direction currentDir;
    private Board myBoard;

    //Pacman constructor
    public Pacman(Pane pane, Board board, int x, int y){
        super(pane, x, y);
        this.myBoard = board;
        this.currentDir = Direction.LEFT;
        this.setSize(14);
        this.setColor(Color.YELLOW);
    }

    //this method deals with moving pacman depending on the direction it's moving
    public void move(){
        switch (this.currentDir){
            case UP:
                if (canMoveUp(this.myBoard) == true){
                    this.moveUp();
                }
                break;
            case DOWN:
                if (this.canMoveDown(this.myBoard)== true) {
                    this.moveDown();
                }
                break;
            case LEFT:
                if (this.getCoordinates()[0] == 15) {
                    this.setLocation(675, this.getCoordinates()[1]);
                } else if (this.canMoveLeft(this.myBoard) == true){
                    this.moveLeft();
                }
                break;
            case RIGHT:
                if (this.getCoordinates()[0] == 675) {
                    this.setLocation(15, this.getCoordinates()[1]);
                } else if (this.canMoveRight(this.myBoard) == true) {
                    this.moveRight();
                }
                break;
            default:
                break;
        }
    }

    // moving up pacman
    private void moveUp(){
        int newY = this.getCoordinates()[1] - 30;
        this.setLocation(this.getCoordinates()[0], newY);
    }

    // moving down pacman
    private void moveDown(){
        int newY = this.getCoordinates()[1] + 30;
        this.setLocation(this.getCoordinates()[0], newY);
    }

    // moving left pacman
    private void moveLeft(){
        int newX = this.getCoordinates()[0] - 30;
        if (newX >0){
            this.setLocation(newX, this.getCoordinates()[1]);}

    }

    // moving right pacman
    private void moveRight(){
        int newX = this.getCoordinates()[0] + 30;
        if (newX < 690){
            this.setLocation(newX, this.getCoordinates()[1]);
        }
    }

    //this method changes the direction, which is basically what we do with the keyPressed method in the game class
    public Direction changeDirection(Direction dir){
        this.currentDir=dir;
        return this.currentDir;
    }

    //checking if pacman can move down, (checking if in that square there is not a wall
    private boolean canMoveDown(Board board) {
        boolean canMoveDown = true;
        int row = this.getCoordinates()[1] / 30;
        int col = this.getCoordinates()[0] / 30;
        if (board.isWall(row+1, col) ==  true){
            canMoveDown = false;
        }
        return canMoveDown;
    }

    //checking if pacman can move up , (checking if in that square there is not a wall
    private boolean canMoveUp(Board board) {
        boolean canMoveUp = true;
        int row = this.getCoordinates()[1] / 30;
        int col = this.getCoordinates()[0] / 30;
        if (board.isWall(row-1, col) ==  true){
            canMoveUp = false;
        }
        return canMoveUp;
    }

    //checking if pacman can move left, (checking if in that square there is not a wall
    private boolean canMoveLeft(Board board) {
        boolean canMoveLeft = true;
        int row = this.getCoordinates()[1] / 30;
        int col = this.getCoordinates()[0] / 30;
        if (board.isWall(row, col-1) ==  true){
            canMoveLeft = false;
        }
        return canMoveLeft;
    }

    //checking if pacman can move right, (checking if in that square there is not a wall
    private boolean canMoveRight(Board board ) {
        boolean canMoveRight = true;
        int row = this.getCoordinates()[1] / 30;
        int col = this.getCoordinates()[0] / 30;
        if (board.isWall(row, col+1) ==  true){
            canMoveRight = false;
        }
        return canMoveRight;
    }

}
