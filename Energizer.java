package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import javafx.scene.layout.Pane;

import static cs15.fnl.pacmanSupport.CS15SquareType.ENERGIZER;

public class Energizer extends Dot implements Collidable{
    private int energizerScore;

    public Energizer(Pane pane, int x, int y) {
        super(pane, x, y);
        this.setSize(8);
        this.energizerScore = 100;
    }

    @Override
    public int getScore(){
        return this.energizerScore;
    }

    @Override
    public CS15SquareType getType() {
        return ENERGIZER;
    }

}
