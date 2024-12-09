package pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class PaneOrganizer {
    private BorderPane root;
    private sideBar sideBar;
    public PaneOrganizer(){
        this.root = new BorderPane();
        this.setBottomPane();
        this.setGamePane();

    }

    private void setGamePane(){
       Pane gamePane = new Pane();
       gamePane.setStyle("-fx-background-color:#900C3F");
       gamePane.setPrefSize(690, 690);
       gamePane.setFocusTraversable(true);
       new Game(gamePane, this.sideBar);
       this.root.setCenter(gamePane);

    }

    private void setBottomPane(){
        HBox bottomPane = new HBox();
        this.sideBar = new sideBar(bottomPane);
        this.root.setBottom(bottomPane);

    }

    public BorderPane getRoot(){
        return this.root;
    }

}
