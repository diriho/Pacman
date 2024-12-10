/**this class initializes the Board and populate it with Black squares, initialize the maze map
 * and the add the walls **/

package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Board {
    private CS15SquareType[][] myMaze;
    private Pane pane;
    private Square[][] myBoard;

    //Board constructor
    public Board(Pane pane){
        this.pane = pane;
        this.myMaze = cs15.fnl.pacmanSupport.CS15SupportMap.getSupportMap();
        this.setMyBoard();

    }

    /**this method set's the Board, by populating the board with Blue Square (where there are wall) and Black to
     * other places**/
    private void setMyBoard(){
        //setting the smartSquares of the board
        this.myBoard = new Square[23][23];
        for (int i = 0; i<this.myBoard.length; i++) {
            for (int j = 0; j < this.myBoard[0].length; j++) {
                this.myBoard[j][i] = new Square(this.pane, this.myMaze[j][i]);
                this.myBoard[j][i].setLocation(i*30, j*30);
                if (this.myBoard[j][i].getType() == CS15SquareType.WALL){
                    this.myBoard[j][i].setColor(Color.BLUE);
                } else {
                this.myBoard[j][i].setColor(Color.BLACK);

                    }
            }
        }

    }

    //check if a particular square of the Board is a WALL
    public boolean isWall(int rw, int col){
        if (this.myBoard[rw][col].getType() == CS15SquareType.WALL){
            return true;
        }
        return false;
    }

    //this method returns the Board
    public Square[][] getBoard(){
        return this.myBoard;
    }

    //this method resets the Boards to its initial location
    public void resetBoard(){
        for (int rw =0; rw<this.myBoard.length; rw++){
            for (int col=0; col<this.myBoard[0].length; col++){
                this.pane.getChildren().remove(this.myBoard[rw][col]);
                this.myBoard[rw][col] = null;
            }
        }
        this.setMyBoard();
    }

}

