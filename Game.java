package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Game {
    private Pacman pacman;
    private Board gameBoard;
    private Pane myPane;
    private int score;
    private int lives;
    private  int [][] ghostInitialLocation;
    private Timeline timeline;
    private sideBar sideBar;
    private Ghost[] ghosts;
    private int penCounter;
    private int counter;
    private Queue<Ghost> ghostInPen;
    public Game(Pane pane, sideBar sideBar){
        this.myPane = pane;
        this.score = 0;
        this.counter = 0;
        this.lives = 3;
        this.ghostInPen = new LinkedList<>();
        this.penCounter = 0;
        this.sideBar = sideBar;
        this.setGameBoard();
        this.update();

    }

    private void setGameBoard() {
        this.gameBoard = new Board(this.myPane);
        for (int row=0; row<this.gameBoard.getBoard().length; row++) {
            for (int col = 0; col < this.gameBoard.getBoard()[0].length; col++) {
                //create pacman and set its specific initial location
                if (this.gameBoard.getBoard()[row][col].getType()== CS15SquareType.PACMAN_START_LOCATION) {
                    this.pacman = new Pacman(this.myPane,this.gameBoard, col*30 + 15, row*30 + 15);

                //create all the dots to their specific locations
                } else if(this.gameBoard.getBoard()[row][col].getType() == CS15SquareType.DOT){
                    Dot dot = new Dot(this.myPane, col*30 + 15, row*30 + 15);
                    //dot.setLocation(col*30 + 15, row*30 + 15);
                    this.gameBoard.getBoard()[row][col].addElement(dot);

                //create all the energizers to their specific location
                } else if (this.gameBoard.getBoard()[row][col].getType() == CS15SquareType.ENERGIZER){
                    Energizer energizer = new Energizer(this.myPane, col*30 + 15, row*30 + 15);
                    //energizer.setLocation(col*30 + 15, row*30 + 15);
                    this.gameBoard.getBoard()[row][col].addElement(energizer);

                //create all the ghosts to their specific initial location
                } else if (this.gameBoard.getBoard()[row][col].getType() == CS15SquareType.GHOST_START_LOCATION){
                    this.ghosts = new Ghost[4];
                    int x = col * 30;
                    int y =  row * 30;
                    this.ghostInitialLocation = new int[][] {{x, y}, {x + 30, y}, {x - 30, y}, {x, y - 60}};
                    for (int i=0; i<ghostInitialLocation.length; i++ ){
                        this.ghosts[i] = new Ghost(this.myPane, Constants.GHOST_COLORS[i], this.gameBoard, ghostInitialLocation[i]);
                        if (i<3){
                            this.ghostInPen.add(this.ghosts[i]);
                        }
                    }



                }
            }
        }
    }

    private void checkCollision(){
        int row = this.pacman.getCoordinates()[1]/30;
        int col = this.pacman.getCoordinates()[0]/30;
        Square currentSquare = this.gameBoard.getBoard()[row][col];
        if (currentSquare.getSquareElements().size() != 0) {
            for (int i =0; i< currentSquare.getSquareElements().size(); i++) {
                if (currentSquare.getSquareElements().get(i).getType() == CS15SquareType.ENERGIZER){
                    for (Ghost ghost:this.ghosts){
                        ghost.setCurrBehaviour(ghostBehavior.FRIGHTENED);
                        ghost.setCounter(0);
                        ghost.setColor(Color.SKYBLUE);
                    }
                }
                this.score += currentSquare.getSquareElements().get(i).getScore();
                currentSquare.getSquareElements().get(i).executeCollision();
                currentSquare.removeElement(currentSquare.getSquareElements().get(i));
                this.sideBar.changeScoreLabel("Score: "+this.score);

            }
        }
    }


    private void update(){
        KeyFrame keyFrame = new KeyFrame(Duration.millis(300), (ActionEvent e) -> {
            this.moveFromPen();
            this.pacman.move();
            this.checkCollision();
            int row = this.pacman.getCoordinates()[1]/30;
            int col = this.pacman.getCoordinates()[0]/30;
            BoardCoordinate target = new BoardCoordinate(row, col, true);
            //System.out.println(this.ghostInPen.size());

            /*remember that in this particulae scenariio you are goign to set ins a such a way that before doing the f
            * for loop, you first check the curent state of one o the ghosts and do the remaining logic
            * if it reveals to not be the right option, change it to be other as it was initially*/
            for (Ghost ghost:this.ghosts){
                ghost.updateState();
                if(ghost.getCurrBehaviour() == ghostBehavior.SCATTER){
                    ghost.moveGhost(getScatterTarget(ghost));
                } else if (ghost.getCurrBehaviour() == ghostBehavior.CHASE) {
                    ghost.moveGhost(getChaseTarget(ghost));
                } else if (ghost.getCurrBehaviour() == ghostBehavior.FRIGHTENED){
                    ghost.moveGhostInFrightened();
                }
            }

            this.checkGhostCollision();
        }
        );
        this.timeline = new Timeline(keyFrame);
        this.myPane.setOnKeyPressed((KeyEvent e) -> this.onKeyPressed(e));
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();

    }

    private void onKeyPressed(KeyEvent e){
        int currRow = this.pacman.getCoordinates()[1]/30;
        int currCol = this.pacman.getCoordinates()[0]/30;
        KeyCode keyPressed = e.getCode();
        switch(keyPressed){
            case UP:
                if ( !this.gameBoard.isWall(currRow-1, currCol)){
                    this.pacman.changeDirection(Direction.UP);
                }
                break;
            case DOWN:
                if ( !this.gameBoard.isWall(currRow+1, currCol)) {
                    this.pacman.changeDirection(Direction.DOWN);
                }

                break;
            case LEFT:
                if ( !this.gameBoard.isWall(currRow, currCol-1)) {
                    this.pacman.changeDirection(Direction.LEFT);
                }
                break;
            case RIGHT:
                if ( !this.gameBoard.isWall(currRow, currCol+1)) {
                    this.pacman.changeDirection(Direction.RIGHT);
                }

                break;
            default:
                break;
        }
        e.consume();
    }


//    private void printMap(Direction[][] searchMap){
//        for (int i = 0; i < 23; i++) {
//            for (int j = 0; j < 23; j++) {
//                if (searchMap[i][j] == null) {
//                    System.out.print("\t");
//                } else {
//                    System.out.print(searchMap[i][j].toString().charAt(0) + "\t");
//                }
//            }
//            System.out.println();
//        }
//    }


    private void moveFromPen(){
        this.penCounter+=1;
        if (!this.ghostInPen.isEmpty() && this.penCounter ==10) {
            this.ghostInPen.remove().setLocation(330, 240);
            this.penCounter = 0;
        }
    }

    private void checkGhostCollision() {
        ghostBehavior currentMode = this.ghosts[0].getCurrBehaviour();
        int row = this.pacman.getCoordinates()[1] / 30;
        int col = this.pacman.getCoordinates()[0] / 30;
        if (currentMode == ghostBehavior.FRIGHTENED) {
            for (Ghost myGhost : this.ghosts) {
                int ghostRow = myGhost.getCoordinates()[1] / 30;
                int ghostCol = myGhost.getCoordinates()[0] / 30;
                if (row == ghostRow && col == ghostCol) {
                    this.score += myGhost.getScore();
                    myGhost.executeCollision();
                    this.ghostInPen.add(myGhost);
                    this.penCounter = 0;
                }
            }

        } else {
            if (isCollided()) {
                this.timeline.stop();
                this.ghostInPen.clear();
                this.gameBoard.resetBoard();
                this.setGameBoard();
                this.lives -= 1;
                this.sideBar.changeLivesLabel("Lives: " + this.lives);
                this.penCounter = 0;
                this.update();

            }

        }

    }
    private boolean isCollided() {
        int row = this.pacman.getCoordinates()[1] / 30;
        int col = this.pacman.getCoordinates()[0] / 30;
        for (Ghost myGhost : this.ghosts) {
            int ghostRow = myGhost.getCoordinates()[1] / 30;
            int ghostCol = myGhost.getCoordinates()[0] / 30;
            if (row == ghostRow && col == ghostCol) {
                return true;
            }
        }
        return false;
    }


    private BoardCoordinate getChaseTarget(Ghost ghost) {
        int row = pacman.getCoordinates()[1]/30;
        int col = pacman.getCoordinates()[0]/30;
        if (ghost.getMyColor() == Color.RED) {
            return new BoardCoordinate(row, col, true); //here I return the pacman position (row, col)
        } else if (ghost.getMyColor() == Color.HOTPINK) {
            return new BoardCoordinate(row+1, col+3, true); //new BoardCoordinate(0, 22, true); here I return the pacman position (col +2), isTarget(true
        } else if (ghost.getMyColor() == Color.SKYBLUE) {
            return new BoardCoordinate(row, col+2, true); //new BoardCoordinate(22, 0, true); here I return the pacman position (row -4), isTarget(true
        } else if (ghost.getMyColor() == Color.ORANGE){
            return new BoardCoordinate(row-4, col, true); //new BoardCoordinate(22, 22, true); here I return the pacman position (row -3) (col +1), isTarget(true
        }
        else {
            return null;
        }
    }

    public BoardCoordinate getScatterTarget(Ghost ghost) {
        if (ghost.getMyColor() == Color.RED) {
            return new BoardCoordinate(0, 0, true);
        } else if (ghost.getMyColor() == Color.HOTPINK) {
            return new BoardCoordinate(0, 22, true);
        } else if (ghost.getMyColor() == Color.SKYBLUE) {
            return new BoardCoordinate(22, 0, true);
        } else if (ghost.getMyColor() == Color.ORANGE) {
            return new BoardCoordinate(22, 22, true);
        } else {
            return null;
        }
    }

}
