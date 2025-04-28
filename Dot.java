/**The Dot class serves as a superclass (parent class for Energizer and Pacman)*
 * It also implements the Collidable interface*/

package Pacman;

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

    //Dot constructor
    public Dot(Pane pane, int x, int y){
        this.myPane = pane;
        this.x = x;
        this.y = y;
        this.dot = new Circle(this.x, this.y, Constants.DOT_RADIUS);
        this.dot.setFill(Color.WHITE);
        this.myPane.getChildren().add(this.dot);

        this.dotScore = Constants.DOT_SCORE;
    }

    //this method sets the location of the dot to a new location
    public void setLocation (int x, int y){
        this.dot.setCenterX(x);
        this.dot.setCenterY(y);
    }

    //this method sets the color of the dots (circle
    public void setColor(Paint color){
        this.dot.setFill(color);
    }

    /*this is a helper method which returns the coordinates  */
    @Override
    public int[] getCoordinates(){
        int[] coordinate = new int [Constants.COORDS_SIZE];
        coordinate[0] = (int) this.dot.getCenterX();
        coordinate[1] = (int) this.dot.getCenterY();
        return coordinate;
    }

    //this method is about executing the collision, which is basically removing graphically the dot
    @Override
    public void executeCollision(){
        this.myPane.getChildren().remove(this.dot);
    }
    //this method returns the type of this, I am setting it to null, because I will not use it anywhere
    @Override
    public SquareType getType() {
        return SquareType.DOT;
    }

    //this method resets the radius of the circle to a new value
    public void setSize(int radius){
        this.dot.setRadius(radius);
    }

    //this method returns the score of the dot
    @Override
    public int getScore() {
        return this.dotScore;
    }
}
