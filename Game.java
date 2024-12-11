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
    private Queue<Collidable> ghostInPen;

    /*Game constructor*/
    public Game(Pane pane, sideBar sideBar){
        this.myPane = pane;
        this.score = 0;
        this.lives = Constants.LIVES;
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
                    this.pacman = new Pacman(this.myPane, this.gameBoard, col*Constants.SQUARE_WIDTH + Constants.OFFSET,
                            row*Constants.SQUARE_WIDTH + Constants.OFFSET);

                //create all the dots to their specific locations, and add them in the square's arrayList
                } else if(this.gameBoard.getBoard()[row][col].getType() == CS15SquareType.DOT){
                    Dot dot = new Dot(this.myPane, col*Constants.SQUARE_WIDTH + Constants.OFFSET,
                            row*Constants.SQUARE_WIDTH + Constants.OFFSET);
                    this.gameBoard.getBoard()[row][col].addElement(dot);

                //create all the energizers to their specific location, and add them in the square's arrayList
                } else if (this.gameBoard.getBoard()[row][col].getType() == CS15SquareType.ENERGIZER){
                    Energizer energizer = new Energizer(this.myPane, col*Constants.SQUARE_WIDTH + Constants.OFFSET,
                            row*Constants.SQUARE_WIDTH + Constants.OFFSET);
                    this.gameBoard.getBoard()[row][col].addElement(energizer);

                //create all the ghosts to their specific initial location, and add them in the square's arrayList
                } else if (this.gameBoard.getBoard()[row][col].getType() == CS15SquareType.GHOST_START_LOCATION){
                    this.ghosts = new Ghost[Constants.GHOSTS_NUM];
                    int x = col * Constants.SQUARE_WIDTH;
                    int y =  row * Constants.SQUARE_WIDTH;
                    int [][] ghostInitialLocation = new int[][] {{x, y}, {x + Constants.SQUARE_WIDTH, y},
                            {x - Constants.SQUARE_WIDTH, y}, {x, y - 2*Constants.SQUARE_WIDTH}};
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

    /**this method checks for collisions when pacman collide with elements that are in the arraylist of each square
     * that pacman steps into - either it be: dots , energizers - and ghosts
     * I made an if statement because the collision handling occur differently depending on the kind of mode
     * does this collided ghost find itself to be at that particular moment **/
    private void checkCollision(){
        /*getting the location of the pacman (row & col), which corresponds to the square in which pacman
         *will find itself to be
        * here I am checking the first ghost because all at once the ghosts will be in the same mode at the same time
        * It would be the same thing if I had checked for any of other ghost*/
        int row = this.pacman.getCoordinates()[1]/Constants.SQUARE_WIDTH;
        int col = this.pacman.getCoordinates()[0]/Constants.SQUARE_WIDTH;
        ghostBehavior currentMode = this.ghosts[0].getCurrBehaviour();

        /*iterate through the whole arrayList of collidable elements in that square*/
        Square currentSquare = this.gameBoard.getBoard()[row][col];
        if (currentSquare.getSquareElements().size() != 0) {
            for (int i =0; i< currentSquare.getSquareElements().size(); i++) {
                //if you find an energizer there, set all the ghost to FRIGHTENED mode and change their color to White
                if (currentSquare.getSquareElements().get(i).getType() == CS15SquareType.ENERGIZER){
                    for (Ghost ghost:this.ghosts){
                        ghost.setCurrBehaviour(ghostBehavior.FRIGHTENED);
                        ghost.setCounter(0);
                        ghost.setColor(Color.WHITE);
                    }

                    //Increment the score and remove logically and graphically the collided thing - Energizer
                    this.score += currentSquare.getSquareElements().get(i).getScore();
                    currentSquare.getSquareElements().get(i).executeCollision();
                    currentSquare.removeElement(currentSquare.getSquareElements().get(i));
                    this.sideBar.changeScoreLabel("Score: "+this.score);

                /*if you find a ghost there, set all the ghost to FRIGHTENED mode and change their color to White
                * otherwise, bring back pacman and ghosts to their initial location,
                * reset the pen counter to 0, and reduce lives by one*/
                } else if (currentSquare.getSquareElements().get(i).getType() == CS15SquareType.GHOST_START_LOCATION){
                    if (currentMode == ghostBehavior.FRIGHTENED){
                        this.score += currentSquare.getSquareElements().get(i).getScore();
                        this.ghostInPen.add(currentSquare.getSquareElements().get(i));
                        currentSquare.getSquareElements().get(i).executeCollision();
                        currentSquare.removeElement(currentSquare.getSquareElements().get(i));
                        this.penCounter = 0;
                        this.sideBar.changeScoreLabel("Score: "+this.score);
                    } else {
                        this.timeline.pause();
                        int collidedRow = this.pacman.getCoordinates()[1]/Constants.SQUARE_WIDTH;
                        int collidedCol = this.pacman.getCoordinates()[0]/Constants.SQUARE_WIDTH;
                        this.gameBoard.getBoard()[collidedRow][collidedCol].getSquareElements().clear();
                        this.resetPacmanAndGhost();
                        this.lives -= 1;
                        this.sideBar.changeLivesLabel("Lives: " + this.lives);
                        this.penCounter = 0;
                        this.setUpTimeLine();

                    }
                /*finally, if you find there a dot, Increment the score
                and remove logically and graphically the collided thing - Dot*/
                } else {
                    this.score += currentSquare.getSquareElements().get(i).getScore();
                    currentSquare.getSquareElements().get(i).executeCollision();
                    currentSquare.removeElement(currentSquare.getSquareElements().get(i));
                    this.sideBar.changeScoreLabel("Score: " + this.score);
                }
            }
        }
    }


    /*this helper method dictates what happens when the game is over, the timeline stops and the label of Game over */
    private void gameOver(){
        this.timeline.stop();
        Label gameOver = new Label("Game Over");
        gameOver.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, Constants.GAME_OVER_SIZE));
        gameOver.setTextFill(Color.GOLD);
        this.myPane.getChildren().add(gameOver);

    }

    /*this method explains the logic of what should happen everytime in the timeline.
    * Pacman moves - the ghosts inside the pen are being removed out-side of the pen - I check for collisions with
    * dots and energizers - move the ghost - and check for collisions for the second time
    * ht */
    private void updateGame(){
        this.moveFromPen();
        this.pacman.move();
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
                ghost.moveGhost(null);
            }
        }
        this.checkCollision();

    }

    //this method initialize the timeline and checks if the game is not overs, if not, you to the update_game
    private void setUpTimeLine(){
        KeyFrame keyFrame = new KeyFrame(Duration.millis(Constants.KEYFRAME_TIME), (ActionEvent e) -> {
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


    /*this method handle the key pressing, which basically is, everytime, I press a key arrow, I am changing
    * the direction of pacman*/
    private void onKeyPressed(KeyEvent e) {
        int currRow = this.pacman.getCoordinates()[1] / Constants.SQUARE_WIDTH;
        int currCol = this.pacman.getCoordinates()[0] / Constants.SQUARE_WIDTH;
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
            this.ghostInPen.remove().setLocation(Constants.JUMP_X, Constants.JUMP_Y);
            this.penCounter = 0;
        }
    }


    /*this helper method is a method that resets pacman and all the ghosts to their initial location
    * this method  will be called when pacman collide with the ghost in the scatter or chase mode*/
    private void resetPacmanAndGhost(){
        this.ghostInPen.clear();
        this.pacman.setLocation(this.pacman.getInitialLocation()[0], this.pacman.getInitialLocation()[1]);
        for (int i=0; i<this.ghosts.length; i++){
            this.ghosts[i].setLocation(this.ghosts[i].getInitialLocation()[0], this.ghosts[i].getInitialLocation()[1]);
            if (i<3){
                this.ghostInPen.add(this.ghosts[i]);
            }
        }

    }

    /*this method returns the Target of each individual ghost when they are in CHASE Mode. It identifies each
    individual ghost depending on its color*/
    private BoardCoordinate getChaseTarget(Ghost ghost) {
        int row = pacman.getCoordinates()[1]/Constants.SQUARE_WIDTH;
        int col = pacman.getCoordinates()[0]/Constants.SQUARE_WIDTH;
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

    /*this is a helper method which checks if the game is over, by returning a boolean in the pacman lives
    * reaches 0 */
    private boolean isGameOver(){
        if (this.lives == Constants.O_VALUE){
            return true;
        } else {
            return false;
        }
    }

}
