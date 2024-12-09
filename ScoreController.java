package pacman;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;



public class ScoreController  {
    private int score = 0;
    private Label scoreLabel;
    public ScoreController(Pane pane){
        this.scoreLabel = new Label("Score: "+this.score);
        pane.getChildren().add(this.scoreLabel);

    }
    public int updateScore(Collidable element){
        this.score += element.getScore();
        return this.score;
    }

}
