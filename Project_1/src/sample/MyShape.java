package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/*
* Defines point(x,y) and color of a shape
*/

public class MyShape {

    public static double x,y;
    String description;
    Color color;

    // X coordinates
    public double getX(){
        return x;
    }

    // Y Coordinates
    public double getY(){
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) { this.y = y; }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    // Returns string description
    public String toString() {
        return description;
    }
    // Draws a figure onto the canvas
    public void draw(GraphicsContext graphics) {
        graphics.setFill(color);
    }



}
