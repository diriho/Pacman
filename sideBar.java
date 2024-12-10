package pacman;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class sideBar {
    private HBox barPane;
    private Label scoreLabel;
    private Button btn;
    private Label lifeLabel;
    public sideBar(HBox bar){
        this.barPane = bar;
        this.setUpSideBar();
    }


    private void setUpSideBar(){
        this.btn = new Button("Quit");
        this.scoreLabel = new Label("Score: 0");

        this.lifeLabel = new Label("Lives: 3");
        this.lifeLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        this.lifeLabel.setTextFill(Color.WHITE);

        this.scoreLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        this.scoreLabel.setTextFill(Color.WHITE);

        this.btn.setFocusTraversable(false);
        this.barPane.setFocusTraversable(false);
        this.btn.setOnAction((ActionEvent e) -> System.exit(0));
        this.barPane.getChildren().addAll(btn,  this.lifeLabel, this.scoreLabel);
        this.barPane.setAlignment(Pos.CENTER);
        this.barPane.setSpacing(20);
        this.barPane.setStyle("-fx-background-color:#900C3F");
    }

    public void changeScoreLabel(String s){
        this.scoreLabel.setText(s);
    }

    public void changeLivesLabel(String s){
        this.lifeLabel.setText(s);
    }

}
