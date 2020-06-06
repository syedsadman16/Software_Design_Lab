package sample;

// Defined by radius r and center(x,y)
// Fill with any color

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class MyCircle extends MyShape{

    double radius, x, y;
    Color color;

    public MyCircle(double radius, double x_coord, double y_coord, Color color) {
        this.radius = radius;
        this.x = x_coord - radius/2;
        this.y = y_coord - radius/2;
        this.color = color;
    }

    // Area of the circle
    public double getArea(){
        double area = (Math.PI * radius * radius);
        return area;
    }

    // Perimeter of Circle
    public double getPerimeter(){
        double result = 2 * Math.PI * Math.sqrt((Math.pow(x/2,2) + Math.pow(y/2,2)) /2);
        return result;
    }

    // Inner radius of circle
    public double getRadius(){
        return radius;
    }

    @Override
    public String toString() {
        return "MyCircle: Radius = " + getRadius() + " Perimeter = " + getPerimeter() + " Area = " + getArea();
    }

    @Override
    public void draw(GraphicsContext graphics) {
        System.out.println(toString());
        graphics.setFill(color);
        // Use the same radius to create circle
        graphics.fillOval(x,y, getRadius(), getRadius());
    }



}
