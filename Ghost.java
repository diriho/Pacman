package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import java.util.LinkedList;
import java.util.Queue;


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

    public void setCounter(int value){
        this.counter = value;
    }

    private void setGhost(Paint color){
        this.ghost = new Rectangle(30, 30, color);
        this.setLocation(this.initialLocation[0], this.initialLocation[1]); //this 1D array will have the coordinates info for a ghost
        this.pane.getChildren().add(this.ghost) ;
    }

    public void updateState() {
        this.counter += 1;
        //System.out.println(counter);
        if (this.currBehaviour == ghostBehavior.FRIGHTENED && this.counter == 12) {
            //System.out.println(this.counter);
            this.setCurrBehaviour(ghostBehavior.CHASE);
            this.setColor(Color.GRAY);
            this.counter = 0;
        }  else if (this.currBehaviour == ghostBehavior.SCATTER && this.counter == 30) {
             //System.out.println(this.counter);
            this.setCurrBehaviour(ghostBehavior.CHASE);
            this.setColor(Color.GRAY);
            this.counter = 0;// Reset counter after switching states}
        } if (this.currBehaviour == ghostBehavior.CHASE && this.counter == 12) {
            //System.out.println(this.counter);
            this.setCurrBehaviour(ghostBehavior.SCATTER);
            this.setColor(Color.CHOCOLATE);
            this.counter = 0; // Reset counter after switching states
        }
        //System.out.println(this.counter);
        //System.out.println(this.currBehaviour);
    }

//    public void frightenedMode(){
//        this.currBehaviour = ghostBehavior.FRIGHTENED;
//        this.ghost.setFill(Color.SKYBLUE);
//        this.counter = 0;
//    }

    public Direction  BFS(BoardCoordinate target){
        int row = this.getCoordinates()[1]/30;
        int col = this.getCoordinates()[0]/30;
        Direction [][] map = new Direction[23][23];
        Queue<BoardCoordinate> myQueue = new LinkedList<>();
        int minimumDist = (int) Double.POSITIVE_INFINITY;
        BoardCoordinate ghostLoc = new BoardCoordinate(row, col, false);

        this.getNeighbors(myQueue, ghostLoc, map, true);
        while (!myQueue.isEmpty()){
            BoardCoordinate currentNeighbor = myQueue.remove();
            this.getNeighbors(myQueue, currentNeighbor, map, false);
            int neighborDist = (int) Math.hypot((currentNeighbor.getRow()-target.getRow()), (currentNeighbor.getColumn() - target.getColumn()));
            Direction newDir = map[currentNeighbor.getRow()][currentNeighbor.getColumn()];
            if (neighborDist < minimumDist && !newDir.isOpposite(this.currentDir)){
                minimumDist = neighborDist;
                this.currentDir = newDir;
            }
        }
        return this.currentDir;
    }

   private void getNeighbors(Queue<BoardCoordinate> neighbors, BoardCoordinate current, Direction[][] visited, boolean isFirst) {
       int row = current.getRow() ;
       int col = current.getColumn() ;
       if (row > 21 || col >21 || row <= 0 || col <= 0 ){
            //you don't do anything
       } else {
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
           if (this.myBoard.isWall(row, col - 1) == false && visited[row][col - 1] == null) {
               BoardCoordinate neighbor4 = new BoardCoordinate(row, col - 1, false);
               Direction dir = Direction.LEFT;
               neighbors.add(neighbor4);
               if (isFirst == true) {
                   visited[row][col - 1] = dir;
               } else {
                   visited[row][col - 1] = this.currentDir;
               }
           }
       }
   }


    @Override
    public int[] getCoordinates(){
        int[] coordinate = new int [2];
        coordinate[0] = (int) this.ghost.getX();
        coordinate[1] = (int) this.ghost.getY();
        return coordinate;

    }


    public BoardCoordinate getScatterTarget() {
        if (this.myColor == Color.RED) {
            return new BoardCoordinate(0, 0, true);
        } else if (this.myColor == Color.PINK) {
            return new BoardCoordinate(0, 22, true);
        } else if (this.myColor == Color.GREEN) {
            return new BoardCoordinate(22, 0, true);
        } else {
            return new BoardCoordinate(22, 22, true);
         }
    }

    public BoardCoordinate getChaseTarget() {
        if (this.myColor == Color.RED) {
            return null; // new BoardCoordinate(, 0, true); here I return the pacman position (row, col)love
        } else if (this.myColor == Color.PINK) {
            return null; //new BoardCoordinate(0, 22, true); here I return the pacman position (col +2), isTarget(true
        } else if (this.myColor == Color.GREEN) {
            return null; //new BoardCoordinate(22, 0, true); here I return the pacman position (row -4), isTarget(true
        } else {
            return null; //new BoardCoordinate(22, 22, true); here I return the pacman position (row -3) (col +1), isTarget(true
        }
    }

    public void moveGhost(){
        switch (this.currentDir){
            case UP:
                if (canMove()){
                    this.moveUp();
                }
                break;
            case DOWN:
                if (canMove()){
                this.moveDown();}
                break;
            case RIGHT:
                if (this.getCoordinates()[0] == 660) {
                    this.setLocation(0, this.getCoordinates()[1]);
                } else if (canMove()){
                this.moveRight();}
                break;
            case LEFT:
                if (this.getCoordinates()[0] == 0) {
                    this.setLocation(660, this.getCoordinates()[1]);
                } else if (canMove()){
                    this.moveLeft(); }
                break;
            default:
                break;
        }
    }

    public void setLocation(int x, int y){
        this.ghost.setX(x);
        this.ghost.setY(y);
    }

    @Override
    public void executeCollision(){
        this.setLocation(getInitialLocation()[0], getInitialLocation()[1]);
    }
    public void setColor(Color col){
        this.ghost.setFill(col);
    }

    public ghostBehavior getCurrBehaviour(){
        return this.currBehaviour;
    }
    public void setCurrBehaviour(ghostBehavior newMode){
        this.currBehaviour = newMode;
    }

    @Override
    public int getScore() {
        return this.ghostScore;
    }

    @Override
    public CS15SquareType getType() {
        return null;
    }

    private void moveRight(){
        int newX = (int) (this.ghost.getX()+30);
        this.ghost.setX(newX);
    }
    public void moveLeft(){
        int newX = (int) (this.ghost.getX() - 30);
        this.ghost.setX(newX);
    }

    private void moveDown(){
        int newY = (int) (this.ghost.getY() + 30);
        this.ghost.setY(newY);
    }

    private void moveUp(){
        int newY = (int) (this.ghost.getY() - 30);
        this.ghost.setY(newY);
    }

    public int[] getInitialLocation(){
        return this.initialLocation;
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

}
