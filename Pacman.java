package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Pacman extends Dot{

    private Direction currentDir;
    private Board myBoard;

    public Pacman(Pane pane, Board board, int x, int y){
        super(pane, x, y);
        this.myBoard = board;
        this.currentDir = Direction.LEFT;
        this.setSize(14);
        this.setColor(Color.YELLOW);
    }
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

    private void moveUp(){
        int newY = this.getCoordinates()[1] - 30;
        this.getCircle().setCenterY(newY);
    }
    private void moveDown(){
        int newY = this.getCoordinates()[1] + 30;
        this.getCircle().setCenterY(newY);
    }
    private void moveLeft(){
        int newX = this.getCoordinates()[0] - 30;
        if (newX >0){
        this.getCircle().setCenterX(newX);}
    }
    private void moveRight(){
        int newX = this.getCoordinates()[0] + 30;
        if (newX < 690){
            this.getCircle().setCenterX(newX);
        }
    }

    public Direction changeDirection(Direction dir){
        this.currentDir=dir;
        return this.currentDir;
    }

    public Direction getCurrentDir(){
        return this.currentDir;
    }
    private boolean canMoveDown(Board board) {
        boolean canMoveDown = true;
        int row = this.getCoordinates()[1] / 30;
        int col = this.getCoordinates()[0] / 30;
        if (board.isWall(row+1, col) ==  true){
            canMoveDown = false;
        }
        return canMoveDown;
    }

    private boolean canMoveUp(Board board) {
        boolean canMoveUp = true;
        int row = this.getCoordinates()[1] / 30;
        int col = this.getCoordinates()[0] / 30;
        if (board.isWall(row-1, col) ==  true){
            canMoveUp = false;
        }
        return canMoveUp;
    }
    private boolean canMoveLeft(Board board) {
        boolean canMoveLeft = true;
        int row = this.getCoordinates()[1] / 30;
        int col = this.getCoordinates()[0] / 30;
        if (board.isWall(row, col-1) ==  true){
            canMoveLeft = false;
        }
        return canMoveLeft;
    }
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
