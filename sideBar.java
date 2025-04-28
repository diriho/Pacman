/**
 * The class of the Sidebar is the class that is going to handle creation of labels the quit button
 * and do updates during different scenario of the game **/

package Pacman;

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
    private Label modeLabel;
    private Button btn;
    private Label lifeLabel;

    //sideBad constructor
    public sideBar(HBox bar){
        this.barPane = bar;
        this.setUpSideBar();
    }

    //The method sets Up the side Bad initially when the game starts, with buttons, and all the
    private void setUpSideBar(){
        //create my bottom pane labels and button
        this.btn = new Button("Quit");
        this.scoreLabel = new Label("Score: 0");
        this.modeLabel = new Label("Mode: Scatter");

        this.lifeLabel = new Label("Lives: 3");
        this.lifeLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, Constants.TEXT_SIZE));
        this.lifeLabel.setTextFill(Color.WHITE);

        this.scoreLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, Constants.TEXT_SIZE));
        this.scoreLabel.setTextFill(Color.WHITE);

        this.modeLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, Constants.TEXT_SIZE));
        this.modeLabel.setTextFill(Color.WHITE);

        /*setting focus traversable to false for all the labels and button */
        this.btn.setFocusTraversable(false);
        this.scoreLabel.setFocusTraversable(false);
        this.lifeLabel.setFocusTraversable(false);
        this.barPane.setFocusTraversable(false);
        this.modeLabel.setFocusTraversable(false);


        this.btn.setOnAction((ActionEvent e) -> System.exit(0));
        this.barPane.getChildren().addAll(btn,  this.lifeLabel, this.scoreLabel, this.modeLabel);
        this.barPane.setAlignment(Pos.CENTER);
        this.barPane.setSpacing(Constants.SPACING);
        this.barPane.setStyle(Constants.SIDEBAR_COLOR);
    }

    //this method will help when we want to update the Score, after pacman collides with a Collidable element
    public void changeScoreLabel(String s){
        this.scoreLabel.setText(s);
    }

    //this method will help when we want to update the Lives, after pacman collides with a Ghost element
    public void changeLivesLabel(String s){
        this.lifeLabel.setText(s);
    }

    //this method will help when we want to update the Ghost's mode,
    public void changeModeLabel(String s) {this.modeLabel.setText(s);}
}
