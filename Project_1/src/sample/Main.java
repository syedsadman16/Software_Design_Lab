package sample;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Observable;

public class Main extends Application {
    Canvas canvas;

    public void start(Stage stage){

        // Creates a root container to hold the canvas
        AnchorPane root = new AnchorPane();

        // Set size of the canvas
        double x_width = 800;
        double y_height = 500;

        canvas = new Canvas(x_width, y_height);

        // GraphicsContext: library used to issue draw calls to canvas using buffer
        GraphicsContext graphics_context = canvas.getGraphicsContext2D();

        // Call function to draw all the shapes
        createShapes(graphics_context, x_width, y_height);

        // Add to anchor
        root.getChildren().add(canvas);

        // Create and attach scene to stage
        Scene myScene = new Scene(root, x_width, y_height);
        stage.setScene(myScene);
        stage.show();

        // Make dimensions resizable
        root.widthProperty().addListener(new SizeChangedListener(root));
        root.heightProperty().addListener(new SizeChangedListener(root));
    }

    // Function to create the shapes
    // Hexagon, Circle, Hexagon, Circle, Hexagon
    public void createShapes(GraphicsContext graphics_context, double x, double y){

        // Create a new shape positioned at center
        MyShape shape = new MyShape();
        shape.setX(x/2D);
        shape.setY(y/2D);
        shape.setColor(MyColor.getRandomColor());

        //hexagon
        MyPolygon hex = new MyPolygon(shape.getX(), shape.getY(), 6, 200,MyColor.getRandomColor());
        hex.draw(graphics_context);

        //circle
        MyCircle circle = new MyCircle(340, shape.getX(), shape.getY(), MyColor.getRandomColor());
        circle.draw(graphics_context);

        //hexagon
        MyPolygon hex2 = new MyPolygon(shape.getX(), shape.getY(), 6,169,MyColor.getRandomColor());
        hex2.draw(graphics_context);

        // Create a circle
        MyCircle circle2 = new MyCircle(289, shape.getX(), shape.getY(), MyColor.getRandomColor());
        circle2.draw(graphics_context);

        //hexagon
        MyPolygon hex3 = new MyPolygon(shape.getX(), shape.getY(), 6,145,MyColor.getRandomColor());
        hex3.draw(graphics_context);

        // Creates box and lines
        MyLine line = new MyLine(0, 0, x, y);
        MyLine line2 = new MyLine(0, y, x, 0);
        MyLine line3 = new MyLine(0, 0, 0, y);
        MyLine line4 = new MyLine(x, 0, x, y);
        MyLine line5 = new MyLine(0, 0, x, 0);
        MyLine line6 = new MyLine(0, y, x, y);
        line.draw(graphics_context);
        line2.draw(graphics_context);
        line3.draw(graphics_context);
        line4.draw(graphics_context);
        line5.draw(graphics_context);
        line6.draw(graphics_context);

    }



    // Handles the window resized events
    private class SizeChangedListener implements InvalidationListener {

        private final AnchorPane root;

        public SizeChangedListener(AnchorPane root) {
            this.root = root;
        }

        @Override
        public void invalidated(javafx.beans.Observable observable) {
            // Remove previous canvas
            root.getChildren().remove(canvas);
            // Create a new canvas based on changed dimensions
            canvas = new Canvas(root.getWidth(), root.getHeight());
            // Add canvas to root
            root.getChildren().add(canvas);
            // Draw to canvas
            createShapes(canvas.getGraphicsContext2D(), root.getHeight(), root.getWidth());
        }
    }

    // Main Method
    public static void main(String args[]){
        launch(args);
    }
}
