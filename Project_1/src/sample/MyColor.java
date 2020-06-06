package sample;

import java.util.Random;
import javafx.scene.paint.Color;

// this enum represents a COLOR
public enum MyColor {
    BLACK(0,0,0), WHITE(255,255,255);

    int Red, Green, Blue;

    MyColor(int r, int g, int b) {
        this.Red = r;
        this.Green = g;
        this.Blue = b;
    }

    // If a custom color is set
    public Color getColor(){
        return Color.rgb(Red,Green,Blue);
    }

    @Override
    public String toString(){
        return String.format("R:" + Red + " G:" + Green + " B:" +Blue);
    }

    // If random color is preferred
    public static Color getRandomColor(){
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r,g,b);
    }



}