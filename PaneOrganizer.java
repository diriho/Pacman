/**The paneOrganizer class is a high graphical class that models the graphical logic of the game
 * creating the panes and add the panes to the BorderPane which helps the layout**/

package Pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class PaneOrganizer {
    private BorderPane root;
    private sideBar sideBar;

    //PaneOrganizer's constructor
    public PaneOrganizer(){
        this.root = new BorderPane();
        this.setBottomPane();
        this.setGamePane();

    }

    /*this method sets the central gamePane*/
    private void setGamePane(){
       Pane gamePane = new Pane();
       gamePane.setStyle("-fx-background-color:#900C3F");
       gamePane.setPrefSize(690, 690);
       gamePane.setFocusTraversable(true);
       new Game(gamePane, this.sideBar);
       this.root.setCenter(gamePane);

    }

    //This method sets the BottomPan, which is going to have the sideBar
    private void setBottomPane(){
        HBox bottomPane = new HBox();
        this.sideBar = new sideBar(bottomPane);
        this.root.setBottom(bottomPane);

    }

    //this method return the root (borderpane)
    public BorderPane getRoot(){
        return this.root;
    }

}
