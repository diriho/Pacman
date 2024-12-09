package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Dot implements Collidable{
    private Circle dot;
    private Pane myPane;
    private int dotScore;
    private int x;
    private int y;
    public Dot(Pane pane, int x, int y){
        this.myPane = pane;
        this.x = x;
        this.y = y;
        this.dot = new Circle(this.x, this.y, 5);
        this.dot.setFill(Color.WHITE);
        this.myPane.getChildren().add(this.dot);

        this.dotScore = 10;
    }
    public void setLocation (int x, int y){
        this.dot.setCenterX(x);
        this.dot.setCenterY(y);
    }

    public int[] getInitialLocation(){
        int[] initialLocation = {this.x, this.y};
        return initialLocation;
    }

    public void removeObj(){
        this.myPane.getChildren().remove(this.dot);
    }

    public void setColor(Paint color){
        this.dot.setFill(color);
    }

    /*this is a helper method which returns */
    @Override
    public int[] getCoordinates(){
        int[] coordinate = new int [2];
        coordinate[0] = (int) this.dot.getCenterX();
        coordinate[1] = (int) this.dot.getCenterY();
        return coordinate;
    }

    @Override
    public void executeCollision(){
        this.removeObj();
    }
    @Override
    public CS15SquareType getType() {
        return null;
    }


    public void setSize(int radius){
        this.dot.setRadius(radius);
    }

    public Circle getCircle(){
        return this.dot;
    }

    @Override
    public int getScore() {
        return this.dotScore;
    }
}
