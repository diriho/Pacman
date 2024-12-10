/**The game class serves as a high-logic class that models the high logic functionalities of the whole game
 *It initializes and sets the board and add all te necessary components of the game on the pane in their
 * respective places
 * I also initialized the timeline which will make it that the whole every component of the game moves smoothly on
 * the timeline, handles collisions, handle keypress, and finally always updating the game status in terms of
 * pacman lives remaining and the score of the game and checking for the game over,**/

package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.util.LinkedList;
import java.util.Queue;

public class Game {
    /*Initialize all my instance variables*/
    private Pacman pacman;
    private Board gameBoard;
    private Pane myPane;
    private int score;
    private int lives;
    private Timeline timeline;
    private sideBar sideBar;
    private Ghost[] ghosts;
    private int penCounter;
    private Queue<Ghost> ghostInPen;

    /*Game constructor*/
    public Game(Pane pane, sideBar sideBar){
        this.myPane = pane;
        this.score = 0;
        this.lives = 3;
        this.ghostInPen = new LinkedList<>();
        this.penCounter = 0;
        this.sideBar = sideBar;
        this.setGameBoard();
        this.setUpTimeLine();

    }


    /*this method sets the all the elements of the game (i.e dots, energizers, pacman,  and ghosts.
    * another thing it does, it also adds it to the arrayList of that particular square*/
    private void setGameBoard() {
        this.gameBoard = new Board(this.myPane);
        for (int row=0; row<this.gameBoard.getBoard().length; row++) {
            for (int col = 0; col < this.gameBoard.getBoard()[0].length; col++) {

                //create pacman and set its specific initial location
                if (this.gameBoard.getBoard()[row][col].getType()== CS15SquareType.PACMAN_START_LOCATION) {
                    this.pacman = new Pacman(this.myPane,this.gameBoard, col*30 + 15, row*30 + 15);

                //create all the dots to their specific locations, and add them in the square's arrayList
                } else if(this.gameBoard.getBoard()[row][col].getType() == CS15SquareType.DOT){
                    Dot dot = new Dot(this.myPane, col*30 + 15, row*30 + 15);
                    //dot.setLocation(col*30 + 15, row*30 + 15);
                    this.gameBoard.getBoard()[row][col].addElement(dot);

                //create all the energizers to their specific location, and add them in the square's arrayList
                } else if (this.gameBoard.getBoard()[row][col].getType() == CS15SquareType.ENERGIZER){
                    Energizer energizer = new Energizer(this.myPane, col*30 + 15, row*30 + 15);
                    //energizer.setLocation(col*30 + 15, row*30 + 15);
                    this.gameBoard.getBoard()[row][col].addElement(energizer);

                //create all the ghosts to their specific initial location, and add them in the square's arrayList
                } else if (this.gameBoard.getBoard()[row][col].getType() == CS15SquareType.GHOST_START_LOCATION){
                    this.ghosts = new Ghost[4];
                    int x = col * 30;
                    int y =  row * 30;
                    int [][] ghostInitialLocation = new int[][] {{x, y}, {x + 30, y}, {x - 30, y}, {x, y - 60}};
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

    /*this method checks for collisions when pacman collide with elements that are in the arraylist of each square
    * that pacman steps into*/
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
                        ghost.setColor(Color.WHITE);
                    }
                }
                this.score += currentSquare.getSquareElements().get(i).getScore();
                currentSquare.getSquareElements().get(i).executeCollision();
                currentSquare.removeElement(currentSquare.getSquareElements().get(i));
                this.sideBar.changeScoreLabel("Score: "+this.score);

            }
        }
    }

    /*this helper method dictates what happens when the game is over, the timeline stops and the label of Game over */
    private void gameOver(){
        this.timeline.stop();
        Label gameOver = new Label("Game Over");
        gameOver.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 100));
        gameOver.setTextFill(Color.GOLD);
        gameOver.setEffect(new DropShadow());
        this.myPane.getChildren().add(gameOver);

    }

    /*this method explains the logic of what should happen everytime in the timeline.
    * Pacman moves - the ghosts inside the pen are being removed out-side of the pen - I check for collisions with
    * dots and energizers - move the ghost - and check for collisions for the second time
    * ht */
    private void updateGame(){
        this.pacman.move();
        this.moveFromPen();
        this.checkCollision();

        //this parts deals with moving the ghosts depending on their modes, with their specific targets
        for (Ghost ghost : this.ghosts) {
            ghost.updateState();
            this.sideBar.changeModeLabel("Mode: "+ghost.getCurrBehaviour());
            if (ghost.getCurrBehaviour() == ghostBehavior.SCATTER) {
                ghost.moveGhost(getScatterTarget(ghost));
            } else if (ghost.getCurrBehaviour() == ghostBehavior.CHASE) {
                ghost.moveGhost(getChaseTarget(ghost));
            } else if (ghost.getCurrBehaviour() == ghostBehavior.FRIGHTENED) {
                ghost.moveGhostInFrightened();
            }
        }
        this.checkGhostCollision();
    }

    //this method initialize the timeline and checks if the game is not overs, if not, you to the update_game
    private void setUpTimeLine(){
        KeyFrame keyFrame = new KeyFrame(Duration.millis(300), (ActionEvent e) -> {
            if (isGameOver()) {
                gameOver();
            } else {
                updateGame();
            }
        });
        this.timeline = new Timeline(keyFrame);
        this.myPane.setOnKeyPressed((KeyEvent e) -> this.onKeyPressed(e));
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();

    }


    /*this method handle the key pressing */
    private void onKeyPressed(KeyEvent e) {
        int currRow = this.pacman.getCoordinates()[1] / 30;
        int currCol = this.pacman.getCoordinates()[0] / 30;
        KeyCode keyPressed = e.getCode();
        switch (keyPressed) {
            case UP:
                if (!this.gameBoard.isWall(currRow - 1, currCol)) {
                    this.pacman.changeDirection(Direction.UP);
                }
                break;
            case DOWN:
                if (!this.gameBoard.isWall(currRow + 1, currCol)) {
                    this.pacman.changeDirection(Direction.DOWN);
                }

                break;
            case LEFT:
                if (!this.gameBoard.isWall(currRow, currCol - 1)) {
                    this.pacman.changeDirection(Direction.LEFT);
                }
                break;
            case RIGHT:
                if (!this.gameBoard.isWall(currRow, currCol + 1)) {
                    this.pacman.changeDirection(Direction.RIGHT);
                }

                break;
            default:
                break;
        }
        e.consume();
    }

    /*this helper method deals with setting the counter for ghosts moving outside the pen
    * after the counter gets to 10, the ghost moves out from the pen*/
    private void moveFromPen(){
        this.penCounter+=1;
        if (!this.ghostInPen.isEmpty() && this.penCounter ==10) {
            this.ghostInPen.remove().setLocation(330, 240);
            this.penCounter = 0;
        }
    }

    /*this is a method that will check for collision of pacman and any of the ghosts.
    * I made an if statement because the collision handling occur differently depending on which kind of modes
    * does this collided ghost find itself to be at that particular moment */
    private void checkGhostCollision() {
        //here I am checking the first ghost because all at once the ghosts will be in the same mode at the same time\
        //It would be the same thing if I had checked for any of other ghosts
        //And I am getting the corresponding row and column of pacman which I will use when checking for collision
        ghostBehavior currentMode = this.ghosts[0].getCurrBehaviour();
        int row = this.pacman.getCoordinates()[1] / 30;
        int col = this.pacman.getCoordinates()[0] / 30;

        /*if the current_mode is FRIGHTENED, I iterate through the array of ghost, and the ghost which happen to be
        * collided with pacman will return to its initial position (which is defined in the executeCollision method)
        * and reinitialize the counter for ghosts coming out of the pen to zero */
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


        /*otherwise, if the current mode is other than FRIGHTENED, you reset the gameBoard -to its initial condition -
        * and decreases the pacman lives by one, and you start the timeline again*/
        } else {
            if (isCollided()) {
                this.timeline.pause();
                this.ghostInPen.clear();
                this.gameBoard.resetBoard();
                this.setGameBoard();
                this.lives -= 1;
                this.sideBar.changeLivesLabel("Lives: " + this.lives);
                this.penCounter = 0;
                this.timeline.play();
                //this.setUpTimeLine();

            }

        }

    }

    /*this helper method checks if pacman collides with any of the ghosts, which I stored in a Arran of ghosts
    * by returning true in case a collision occured, and false in the other case*/
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

    /*this method returns the Target of each individual ghost when they are in CHASE Mode. It identifies each
    individual ghost depending on its color*/
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

    /*this is a helper method which checks if the game is over, by returning a boolean in the pacman lives
    * reaches 0
    */
    private boolean isGameOver(){
        if (this.lives ==0){
            return true;
        } else {
            return false;
        }
    }

    /*this method returns the Target of each individual ghost when they are in SCATTER Mode. It identifies each
    individual ghost depending on its color. Those targets correspond to the corners of the pane */
    private BoardCoordinate getScatterTarget(Ghost ghost) {
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
