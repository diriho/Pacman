package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Square {
    private Rectangle sqr;
    private Pane myPane;
    private ArrayList<Collidable> squareElements;
    private CS15SquareType myType;


    public Square(Pane pane, CS15SquareType type){
        this.myPane = pane;
        this.myType = type;
        this.squareElements = new ArrayList<>();
        this.sqr = new Rectangle(30, 30, Color.BLUE);
        this.myPane.getChildren().add(this.sqr);

    }

    public void setColor(Paint color){
        this.sqr.setFill(color);
    }
    public CS15SquareType getType(){
        return this.myType;
    }

    public void setLocation(int x, int y){
        this.sqr.setX(x);
        this.sqr.setY(y);
    }
    public int[] getLocation(){
        int[] currLocation = new int [2];
        currLocation[0] = (int) this.sqr.getX();
        currLocation[1] = (int) this.sqr.getY();
        return currLocation;
    }
    public void addElement(Collidable element){
        this.squareElements.add(element);
    }
    public void removeElement(Collidable element){
        this.squareElements.remove(element);
    }
    public ArrayList<Collidable> getSquareElements(){
        return this.squareElements;
    }
}
