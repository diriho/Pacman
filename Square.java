/**The Square class models the capabilities of a square*/

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


    //Square constructor
    public Square(Pane pane, CS15SquareType type){
        this.myPane = pane;
        this.myType = type;
        this.squareElements = new ArrayList<>();
        this.sqr = new Rectangle(30, 30, Color.BLUE);
        this.myPane.getChildren().add(this.sqr);

    }


    //this method sets the color of this square
    public void setColor(Paint color){
        this.sqr.setFill(color);
    }

    //this method returns the type because I made it in a such a way that every square will know its type
    public CS15SquareType getType(){
        return this.myType;
    }

    //this method sets the location of the square on a particular
    public void setLocation(int x, int y){
        this.sqr.setX(x);
        this.sqr.setY(y);
    }

    //this method adds a collidable element to the arrayList of this square
    public void addElement(Collidable element){
        this.squareElements.add(element);
    }

    //this method removes a collidable element to the arrayList of this square
    public void removeElement(Collidable element){
        this.squareElements.remove(element);
    }

    //This method returns arrayList of this square
    public ArrayList<Collidable> getSquareElements(){
        return this.squareElements;
    }
}
