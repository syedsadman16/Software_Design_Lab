package sample;
import javafx.scene.canvas.GraphicsContext;

import javax.sound.sampled.Line;
import java.lang.Math;

public class MyLine extends MyShape {
    double x1,y1,x2,y2;

    // Define the endpoints for the line
    public MyLine(double x1, double y1, double x2, double y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    // Get the length of the line
    public int getLength(){
        return (int) Math.sqrt(((x2-x1)*(x2-x1)) - ((y2-y1)*(y2-y1)));
    }

    // The angle (in degrees) of the MyLine object with the x-axis
    public int get_Xangle(){
        int angle = (int) ((Math.atan2((y2 - y), (x2 - x)))/(Math.PI/180));
        return angle;
    }

    @Override
    public String toString() {
        return "MyLine: Length = " + getLength() + " Angle = " + get_Xangle() + " degrees";
    }

    @Override
    public void draw(GraphicsContext graphics) {
        System.out.println(toString());
        graphics.setLineWidth(5);graphics.strokeLine(x1, y1, x2, y2);
    }




}
