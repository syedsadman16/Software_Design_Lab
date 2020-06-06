
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static sun.awt.geom.Curve.round;


public class MyPieChart {

    double other, total;
    HashMap<String, Double> pieMap = new HashMap<String, Double>();
    ArrayList<Color> colors = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    ArrayList<Double> probabilites = new ArrayList<>();


    public MyPieChart(HashMap<String, Integer> hashMap, int total) {
        this.total = total;

        // Populate pie chart Hashmap with probabilities
        hashMap.forEach((key,value) -> {
            pieMap.put(key, (Double.valueOf(value)/total));
        });

        // Create the labels and add probability for chart
        pieMap.forEach((key,value)->{
            String students = ( (int)( value * total ) ) + "";
            double number = value;
            number = Math.round(number * 100);
            number = number/100;
            probabilites.add(value);
            labels.add(key +" ["+students+"] : "+ number);
        });

    }



    // Create legend with equal spaces with color and label
    public void drawLegend( double x, double y, GraphicsContext gc){
        gc.strokeText("LEGEND", x, y);

        for(int i=0; i<probabilites.size(); i++) {
            gc.setStroke(Color.BLACK);
            gc.fillText( labels.get(i), x + 30, y + 30);
            gc.setFill(colors.get(i));
            gc.fillRect(x, y + 15, 20, 20);
            y+=30;
        }


    }

    public void drawPieChart(double totalWidth, double totalHeight, GraphicsContext graphicsContext) {

        // Position away from center and spaced next to legend
        double x = totalWidth / 2D - 100;
        double y = totalHeight / 2D - 200 ;
        // Starting and ending for each arc
        double start = 0;
        double end;


        Color color;

        // Start where the previous arc ends, and end at an angle calculated by probability
        end = (probabilites.get(0))*360.00;
        for(int i=0; i<probabilites.size(); i++) {
            color = MyColor.getRandomColor();
            graphicsContext.setFill(color);
            colors.add(color);
            graphicsContext.fillArc(x, y,300 ,300,start,end,ArcType.ROUND);
            if(i == probabilites.size()-1){
                break;
            }
            start = start+end; // Get location for previous arc ended
            end = (probabilites.get(i+1))*360.00; // end at the next angle from probability
        }

        // After pie chart complete, draw legend
        drawLegend( 50, 100, graphicsContext);
    }
}
