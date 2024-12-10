/***this method serves as a wrapper method which modals all the capabilities of  other Ghosts
 * With all the necessary encapsulation
 * It also implements the collidable interface*/

package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class Ghost implements Collidable{
    private Rectangle ghost;
    private int ghostScore;
    private Pane pane;
    private Direction currentDir;
    private Board myBoard;
    private Paint myColor;
    private ghostBehavior currBehaviour;
    private int[] initialLocation;
    private int counter;

    //ghost constructor
    public Ghost(Pane pane, Paint color, Board board, int[] location){
        this.pane = pane;
        this.myColor = color;
        this.currentDir = null;
        this.counter = 0;
        this.currBehaviour = ghostBehavior.CHASE;
        this.myBoard = board;
        this.initialLocation = location;
        this.ghostScore = 200;
        this.setGhost(color);
    }

    //this method resets the counter
    public void setCounter(int value){
        this.counter = value;
    }

    //this method deals with setting dimensions of a ghost and adding it to the pane
    private void setGhost(Paint color){
        this.ghost = new Rectangle(30, 30, color);
        this.ghost.setStroke(Color.BLACK);
        this.setLocation(this.initialLocation[0], this.initialLocation[1]); //this 1D array will have the coordinates info for a ghost
        this.pane.getChildren().add(this.ghost) ;
    }

    //this method updates the state of ghosts according to a particular timer/counter
    public void updateState() {
        this.counter += 1;
        if (this.currBehaviour == ghostBehavior.FRIGHTENED && this.counter == 30) {
            this.setCurrBehaviour(ghostBehavior.CHASE);
            this.setColor(this.myColor);
            this.counter = 0;
        }  else if (this.currBehaviour == ghostBehavior.SCATTER && this.counter == 70) {
            this.setCurrBehaviour(ghostBehavior.CHASE);
            this.counter = 0;// Reset counter after switching states}
        } if (this.currBehaviour == ghostBehavior.CHASE && this.counter == 30) {
            this.setCurrBehaviour(ghostBehavior.SCATTER);
            this.counter = 0; // Reset counter after switching states
        }
    }

    /*This method models the BFS algorithm*/
    public Direction  BFS(BoardCoordinate target){
        int row = this.getCoordinates()[1]/30;
        int col = this.getCoordinates()[0]/30;
        Direction [][] map = new Direction[23][23];
        Direction returnDir = null;
        Queue<BoardCoordinate> myQueue = new LinkedList<>();
        double minimumDist = (int) Double.POSITIVE_INFINITY;
        BoardCoordinate ghostLoc = new BoardCoordinate(row, col, false);

        this.getNeighbors(myQueue, ghostLoc, map, true);
        while (!myQueue.isEmpty()){
            BoardCoordinate currentNeighbor = myQueue.remove();
            this.getNeighbors(myQueue, currentNeighbor, map, false);
            double neighborDist = Math.hypot((currentNeighbor.getRow()-target.getRow()), (currentNeighbor.getColumn() - target.getColumn()));
            Direction newDir = map[currentNeighbor.getRow()][currentNeighbor.getColumn()];
            if (neighborDist < minimumDist ){
                minimumDist = neighborDist;
                returnDir = newDir;
            }
        }
        return returnDir;
    }

    //This method deals with getting the neighbors of a particular board_coordinate
   private void getNeighbors(Queue<BoardCoordinate> neighbors, BoardCoordinate current, Direction[][] visited, boolean isFirst) {
       int row = current.getRow() ;
       int col = current.getColumn() ;
       if (row >= 22 || col >=22 || row <= 0 || col <= 0 ){
            //you don't do anything
       } else {
           if(this.currentDir != Direction.DOWN && isFirst) {
               if (myBoard.isWall(row - 1, col) == false && visited[row - 1][col] == null) {

                   BoardCoordinate neighbor1 = new BoardCoordinate(row - 1, col, false);
                   Direction dir = Direction.UP;
                   neighbors.add(neighbor1);
                   if (isFirst == true) {
                       visited[row - 1][col] = dir;
                   } else {
                       visited[row - 1][col] = this.currentDir;

                   }
               }
           }
           if(this.currentDir != Direction.UP && isFirst) {
               if (this.myBoard.isWall(row + 1, col) == false && visited[row + 1][col] == null) {
                   BoardCoordinate neighbor2 = new BoardCoordinate(row + 1, col, false);
                   Direction dir = Direction.DOWN;
                   neighbors.add(neighbor2);
                   if (isFirst == true) {
                       visited[row + 1][col] = dir;
                   } else {
                       visited[row + 1][col] = this.currentDir;
                   }
               }
           }
           if(this.currentDir != Direction.LEFT && isFirst) {
               if (this.myBoard.isWall(row, col + 1) == false && visited[row][col + 1] == null) {
                   BoardCoordinate neighbor3 = new BoardCoordinate(row, col + 1, false);
                   Direction dir = Direction.RIGHT;
                   neighbors.add(neighbor3);
                   if (isFirst == true) {
                       visited[row][col + 1] = dir;
                   } else {
                       visited[row][col + 1] = this.currentDir;
                   }
               }
           } if(this.currentDir != Direction.RIGHT  && isFirst) {
               if (this.myBoard.isWall(row, col - 1) == false && visited[row][col - 1] == null) {
                   BoardCoordinate neighbor4 = new BoardCoordinate(row, col - 1, false);
                   Direction dir = Direction.LEFT;
                   neighbors.add(neighbor4);
                   if (isFirst ) {
                       visited[row][col - 1] = dir;
                   } else {
                       visited[row][col - 1] = this.currentDir;
                   }
               }
           }
       }
   }

   //this method moves the Ghost depending on the direction that is given the BFS method
    public void moveGhost(BoardCoordinate target) {
        if (this.getCoordinates()[0] == 0) {
            this.setLocation(630, this.getCoordinates()[1]);

        } if (this.getCoordinates()[0] == 660) {
            this.setLocation(30, this.getCoordinates()[1]);
        } else {
            Direction dir = this.BFS(target);
            this.currentDir = dir;
            switch (dir) {
                case UP:
                    if (canMove()) {
                        this.moveUp();
                    }
                    break;
                case DOWN:
                    if (canMove()) {
                        this.moveDown();
                    }
                    break;
                case RIGHT:
                    if (canMove()) {
                        this.moveRight();
                    }
                    break;

                case LEFT:
                    if (this.getCoordinates()[0] == 0) {
                        this.setLocation(630, this.getCoordinates()[1]);
                    } else {
                        if (canMove()) {
                            this.moveLeft();
                        }
                        break;
                    }
                default:
                    break;
            }
        }
    }

    //this method sets the location of pacman, to a new location
    public void setLocation(int x, int y){
        this.ghost.setX(x);
        this.ghost.setY(y);
    }

    //this method brings back the ghost in the pen when the ghost collides with pacman  in frightened mode
    @Override
    public void executeCollision(){
        this.setLocation(330, 300);
    }

    //this method sets the color of the ghost
    public void setColor(Paint col){
        this.ghost.setFill(col);
    }

    //this method gets the current mode of the ghost
    public ghostBehavior getCurrBehaviour(){
        return this.currBehaviour;
    }

    //this method returns sets current mode of the ghost to a new one
    public void setCurrBehaviour(ghostBehavior newMode){
        this.currBehaviour = newMode;
    }

    //this method returns the score of the ghost
    @Override
    public int getScore() {
        return this.ghostScore;
    }

    @Override
    //this method returns the type of this, I am setting it to null, because I will not use it anywhere
    public CS15SquareType getType() {
        return null;
    }

    //this method deals with moving right the ghost, graphically
    private void moveRight(){
        int newX = (int) (this.ghost.getX()+30);
        this.ghost.setX(newX);
    }

    //this method deals with moving left the ghost, graphically
    public void moveLeft() {
        int newX = (int) (this.ghost.getX() - 30);
        this.ghost.setX(newX);
    }

    //this method deals with moving down the ghost, graphically
    private void moveDown(){
        int newY = (int) (this.ghost.getY() + 30);
        this.ghost.setY(newY);
    }

    //this method deals with moving up the ghosts, graphically
    private void moveUp(){
        int newY = (int) (this.ghost.getY() - 30);
        this.ghost.setY(newY);
    }


    private boolean canMove(){
        int row = this.getCoordinates()[1]/30;
        int col = this.getCoordinates()[0]/30;
        switch(this.currentDir){
            case LEFT:
                if (this.myBoard.isWall(row, col-1)) {
                    return false;
                }
                break;
            case RIGHT:
                if (this.myBoard.isWall(row, col+1)) {
                    return false;
                }
                break;
            case UP:
                if (this.myBoard.isWall(row-1, col)) {
                    return false;
                }
                break;
            case DOWN:
                if (this.myBoard.isWall(row+1, col)) {
                    return false;
                }
                break;
        }
        return true;
    }

    /*this is a helper method which returns the coordinates of the ghost  */
    @Override
    public int[] getCoordinates(){
        int[] coordinate = new int [2];
        coordinate[0] = (int) this.ghost.getX();
        coordinate[1] = (int) this.ghost.getY();
        return coordinate;
    }

    //this method returns the color of this ghost, because when I will be checking for targets, I am Color-checking
    public Paint getMyColor(){
        return myColor;
    }

    /*this method deals with moving the ghost in frightened mode, which is basically a random movement, which is based
    //on a random generation of the valid directions which the ghosts can take (this entails, the directions with
    //no walls, and not the opposite*/
    public void moveGhostInFrightened() {
        if (this.getCoordinates()[0] == 0) {
            this.setLocation(630, this.getCoordinates()[1]);

        } if (this.getCoordinates()[0] == 660) {
            this.setLocation(30, this.getCoordinates()[1]);
        } else {
            Direction randomDir = getValidDirection();
            this.currentDir = randomDir;
            switch (randomDir) {
                case UP:
                    if (canMove()) {
                        this.moveUp();
                    }
                    break;
                case DOWN:
                    if (canMove()) {
                        this.moveDown();
                    }
                    break;
                case RIGHT:
                    if (this.getCoordinates()[0] == 660) {
                        this.setLocation(0, this.getCoordinates()[1]);
                    } else {
                        if (canMove()) {
                            this.moveRight();
                        }
                        break;
                    }
                case LEFT:
                    if (this.getCoordinates()[0] == 0) {
                        this.setLocation(660, this.getCoordinates()[1]);
                    } else {
                        if (canMove()) {
                            this.moveLeft();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /*this is the method with deals with the random generation on Direction when the ghost is in frightened mode */
   private Direction getValidDirection () {
       ArrayList<Direction> validDirections = new ArrayList<>();
       int currRow = this.getCoordinates()[1] / 30;
       int currCol = this.getCoordinates()[0] / 30;
       if (!this.myBoard.isWall(currRow - 1, currCol)) {
           validDirections.add(Direction.UP);
       }
       if (!this.myBoard.isWall(currRow + 1, currCol)) {
           validDirections.add(Direction.DOWN);
       }
       if (!this.myBoard.isWall(currRow, currCol - 1)) {
           validDirections.add(Direction.LEFT);
       }
       if (!this.myBoard.isWall(currRow, currCol + 1)) {
           validDirections.add(Direction.RIGHT);
       }
       if (validDirections.contains(this.currentDir.opposite())) {
           int idx = validDirections.indexOf(this.currentDir.opposite());
           validDirections.remove(idx);
       }
       Random random = new Random();
       int index = random.nextInt(validDirections.size());
       return validDirections.get(index);
   }
}
