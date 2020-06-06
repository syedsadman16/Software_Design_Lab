package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.sound.sampled.Line;

public class MyPolygon extends MyShape {

    double center_x,center_y,radius;
    int n;
    Color color;


    public MyPolygon(double center_x, double center_y, int n, double radius, Color color){
        this.center_x = center_x;
        this.center_y = center_y;
        this.radius = radius;
        this.color = color;
        this.n = n;
    }

    // Length of just one side of the polygon
    public double getSide(){
        return radius;
    }

    // Area of Hexagon
    public double getArea() {
        return ((getSide() * (Math.cos((2 * Math.PI) / n / 2))) / 2 * n);
    }

    // Perimeter of Hexagon
    public int getPerimeter(){
        int area = 0;
        return area;
    }

    // Interior angle
    public int getAngle(){
        return 360 / n;
    }

    @Override
    public String toString() {
        return "MyPolygon: Side Length = " + getSide() + " Interior Angle = " + getAngle() +
                " Perimeter = " + getPerimeter() + " Area = " + getArea();
    }

    @Override
    public void draw(GraphicsContext graphics) {
        System.out.println(toString());

        // Choose a color to fill
        graphics.setFill(color);

        // Set up array to choose coordinates for Hexagon
        double[] xPoints = new double[6];
        double[] yPoints = new double[6];

        // Populate array with each point
        for (int i=0; i<6; i++) {
            xPoints[i] = (int) (center_x + Math.sin(i * Math.toRadians(60)) * radius);
            yPoints[i] = (int) (center_y + Math.cos(i * Math.toRadians(60)) * radius);
        }

        // Create Hexagon
        graphics.fillPolygon(xPoints, yPoints, 6);
    }



}
