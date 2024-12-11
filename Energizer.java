/*This class models the functions and properties of the Energizer
* It extends the Dot class, and it also it implements the collidable interface*/

package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import javafx.scene.layout.Pane;

import static cs15.fnl.pacmanSupport.CS15SquareType.ENERGIZER;

public class Energizer extends Dot implements Collidable{
    private int energizerScore;

    //Energizer's constructor
    public Energizer(Pane pane, int x, int y) {
        super(pane, x, y);
        this.setSize(Constants.ENERGIZER_RADIUS);
        this.energizerScore = Constants.ENERGIZER_SCORE;
    }

    //this method returns the score of the energizer
    @Override
    public int getScore(){
        return this.energizerScore;
    }

    //this method returns the type of this energizer
    @Override
    public CS15SquareType getType() {
        return ENERGIZER;
    }

}
