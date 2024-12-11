package pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
  * This is the App class where your Pacman game will start.
  * The main method of this application calls the start method..
  *
  * This class initializes the Pane organizer, which is the too-level graphical class
 * and set the scene and show the stage..
  *
  */

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Create top-level object, set up the scene, and show the stage here.
        PaneOrganizer paneOrganizer = new PaneOrganizer();
        Scene myScene = new Scene(paneOrganizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        stage.setScene(myScene);
        stage.setTitle("Pacman!");
        stage.show();

    }

    /*
    * Here is the mainline!
    */
    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
