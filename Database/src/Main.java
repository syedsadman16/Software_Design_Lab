import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static javafx.application.Application.launch;

public class Main extends Application {
static int total = 0;
static ArrayList<Classes> classes = new ArrayList<>();



    public void start(Stage stage){

        Connection conn = null;

        System.out.println("Hello World!");

        // Set dimensions
        double x_width = 1200;
        double y_height = 800;
        stage.setWidth(x_width);
        stage.setHeight(y_height);


        try {
            // Loads the class object for the mysql driver into the DriverManager.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Attempt to establish a connection to the specified database via theDriverManager
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/software_design","syed","sadman");
            if (conn != null)
            {
                System.out.println("We have connected to our database!");

                // Retrieve all Classes information and populate ArrayList to be used in Table
                readClassesList(conn);

                // Takes in GPA HashMap with total count of grades to generate PieChart data
                MyPieChart pieChart = new MyPieChart(displayStudentsPerSemesterGPA(conn, "Spring"), total);

                // Add GUI container to scene
                Scene scene = new Scene(printGUI(x_width, y_height, classes ,pieChart, conn));

                stage.setTitle("Spring GPA CSC 22100");
                stage.setScene(scene);
                stage.setWidth(x_width);
                stage.setHeight(y_height);
                stage.show();

                // Keep the connection open as long as program is running
                Connection finalConn = conn;
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent e) {
                        try {
                            finalConn.close();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        Platform.exit();
                        System.exit(0);
                    }
                });

            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }


    }


    // Takes in Dimensions, Classes object to populate table, PieChart to display graph and database connection for live updates
    public VBox printGUI(double x_width, double y_height, ArrayList<Classes> classes, MyPieChart myPieChart, Connection connection){

        Canvas canvas = new Canvas(x_width/2,y_height/2);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        VBox vbox = new VBox();

        // Main gridPane layout
        GridPane gp = new GridPane();
        gp.setPadding( new Insets(10) );
        gp.setHgap( 4 );
        gp.setVgap( 10 );

        // Surround with Vbox
        VBox.setVgrow(gp, Priority.ALWAYS );

        // Data to populate tables
       final ObservableList<Classes> data = FXCollections.observableArrayList();
       final ObservableList<Integer> studentListData = FXCollections.observableArrayList();

       for(int i=0; i<classes.size(); i++){
            data.add(classes.get(i));
            studentListData.add(classes.get(i).studentID);
       }


       /*
        *  TOP PORTION OF GUI CONTAINING THE TABLE
        */

        // Setup the table to display Classes data
        TableView<Classes> table = new TableView<Classes>();
        final Label label = new Label("Classes Table");
        label.setFont(new Font("Arial", 20));

        TableColumn lastNameCol = new TableColumn("Name");
        lastNameCol.setMinWidth(175);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Classes, Integer>("lastName"));

        TableColumn studentidCol = new TableColumn("Student");
        studentidCol.setMinWidth(175);
        studentidCol.setCellValueFactory(
                new PropertyValueFactory<Classes, Integer>("studentID"));

        TableColumn courseidCol = new TableColumn("Course ID");
        courseidCol.setMinWidth(175);
        courseidCol.setCellValueFactory(
                new PropertyValueFactory<Classes, Integer>("courseID"));

        TableColumn sectionCol = new TableColumn("Section");
        sectionCol.setMinWidth(175);
        sectionCol.setCellValueFactory(
                new PropertyValueFactory<Classes, String>("section"));

        TableColumn yearCol = new TableColumn("Year");
        yearCol.setMinWidth(175);
        yearCol.setCellValueFactory(
                new PropertyValueFactory<Classes, Integer>("year"));

        TableColumn semesterCol = new TableColumn("Semester");
        semesterCol.setMinWidth(175);
        semesterCol.setCellValueFactory(
                new PropertyValueFactory<Classes, String>("semester"));

        TableColumn gpaCol = new TableColumn("GPA");
        gpaCol.setMinWidth(175);
        gpaCol.setCellValueFactory(
                new PropertyValueFactory<Classes, String>("GPA"));

        table.setItems(data);
        table.getColumns().addAll(lastNameCol, studentidCol, courseidCol, sectionCol, yearCol, semesterCol,gpaCol);


        final VBox vboxTable = new VBox();
        vbox.setSpacing(1);
        vbox.setPadding(new Insets(10, 10, 0, 10));
        vbox.getChildren().addAll(label, table);


        /*
         *  RIGHT PORTION OF GUI CONTAINING THE INPUT FIELDS
         */

        // ----------- Left
        Label studentInfoLabel = new Label("Add new student to class");
        studentInfoLabel.setFont(new Font("Arial", 20));

        Label lblFirstName = new Label("First Name");
        TextField tfFirstName = new TextField();

        Label lblLastName = new Label("Last Name");
        TextField tfLastName = new TextField();

        Label lvlSex = new Label("Sex");
        ObservableList<String> severities = FXCollections.observableArrayList("M", "F");
        ComboBox<String> cbSex = new ComboBox<>(severities);

        VBox priorityVBox = new VBox();
        priorityVBox.setSpacing( 1 );
        priorityVBox.setPadding(new Insets(5, 5, 5, 10));
        GridPane.setVgrow(priorityVBox, Priority.ALWAYS);
        priorityVBox.getChildren().addAll( lblFirstName,tfFirstName, lblLastName, tfLastName,lvlSex,cbSex);


        // ---------- Middle
        Label lblGPA = new Label("GPA");
        ObservableList<String> categories = FXCollections.observableArrayList("A", "B", "C", "D", "F");
        ComboBox<String> gpaCategory = new ComboBox<>(categories);

        Label lblSemester = new Label("Semester");
        ObservableList<String> categories2 = FXCollections.observableArrayList( "Spring");
        ComboBox<String> semesterCategory = new ComboBox<>(categories2);

        Label lblYear = new Label("Year");
        ObservableList<String> categories3 = FXCollections.observableArrayList("2020", "2021", "2022");
        ComboBox<String> yearCategory = new ComboBox<>(categories3);

        VBox priorityVBox2 = new VBox();
        priorityVBox2.setSpacing( 1 );
        priorityVBox2.setPadding(new Insets(10, 10, 10, 10));
        GridPane.setVgrow(priorityVBox2, Priority.ALWAYS);
        priorityVBox2.getChildren().addAll( lblGPA,gpaCategory, lblSemester, semesterCategory, lblYear, yearCategory);


        // ----------- Right
        Label lblCourseID = new Label("Course ID");
        TextField tfCourseID = new TextField();

        Label lblsectionID = new Label("Section");
        TextField tfsectionID = new TextField();

        VBox priorityVBox3 = new VBox();
        priorityVBox3.setSpacing( 1 );
        priorityVBox3.setPadding(new Insets(10, 10, 10, 10));
        GridPane.setVgrow(priorityVBox3, Priority.ALWAYS);
        priorityVBox3.getChildren().addAll( lblCourseID,tfCourseID, lblsectionID, tfsectionID);


        HBox hb = new HBox();
        hb.getChildren().addAll(priorityVBox, priorityVBox3, priorityVBox2);
        GridPane.setHgrow(priorityVBox3, Priority.ALWAYS);
        hb.setPadding(new Insets(10, 10, 10, 10));
        hb.setSpacing(3);


        final Button submit = new Button("Create new entry");
        submit.setPadding(new Insets(10, 10, 10, 10));
        submit.setOnAction(new EventHandler<ActionEvent>() {
            StringBuilder errors = new StringBuilder();
            @Override
            public void handle(ActionEvent e) {

                // Make sure input fields aren't empty
                if(tfFirstName.getText().isEmpty() ||
                        tfLastName.getText().isEmpty() ||
                        cbSex.getSelectionModel().getSelectedItem().isEmpty() ||
                        tfCourseID.getText().isEmpty() || tfsectionID.getText().isEmpty() ||
                        yearCategory.getSelectionModel().getSelectedItem().isEmpty() ||
                        semesterCategory.getSelectionModel().getSelectedItem().isEmpty() ||
                        gpaCategory.getSelectionModel().getSelectedItem().isEmpty())
                {
                    errors.append("Field cannot be empty");
                }

                // Create new student
                Student student = new Student(
                        tfFirstName.getText().toString(),
                        tfLastName.getText().toString(),
                        cbSex.getSelectionModel().getSelectedItem());

                // Create new class for the student
                Classes classe = new Classes(Integer.parseInt(tfCourseID.getText()),
                        student.getStudentID(),
                        tfsectionID.getText().toString(),
                        Integer.parseInt(yearCategory.getSelectionModel().getSelectedItem()),
                        semesterCategory.getSelectionModel().getSelectedItem(),
                        gpaCategory.getSelectionModel().getSelectedItem());
                classe.setLastName(student.lastName);

                // Add to Table
                data.add(classe);
                studentListData.add(student.studentID);

                // Push to database
                insertIntoClasses(connection, student, classe );
                readClassesList(connection);

                // Update PieChart
                MyPieChart pieChart = new MyPieChart(displayStudentsPerSemesterGPA(connection, "Spring"), total);
                pieChart.drawPieChart(x_width/2, y_height/2, gc);

                // Clear all the fields
                tfFirstName.setText("");
                tfCourseID.setText("");
                tfLastName.setText("");
                tfsectionID.setText("");
                gpaCategory.valueProperty().set(null);
                yearCategory.valueProperty().set(null);
                semesterCategory.valueProperty().set(null);
                cbSex.valueProperty().set(null);


            }
        });

        // Add some spacing
        Label spacing = new Label("");
        spacing.setPrefHeight(15);

        // -------- Container for Add button
        HBox hbButton = new HBox();
        hbButton.setSpacing(1);
        GridPane.setHgrow(hbButton, Priority.ALWAYS);
        hbButton.setPadding(new Insets(10, 10, 10, 20));
        hbButton.getChildren().addAll(submit);


        // ------- Bottom
        Label lblStudentsList = new Label("Remove a student");
        lblStudentsList.setFont(new Font("Arial", 20));

        ObservableList<Integer> studentListCategory = FXCollections.observableArrayList(studentListData);
        ComboBox<Integer> cbstudentList = new ComboBox<Integer>(studentListCategory);

        // ------- Button to delete from table
        final Button delete = new Button("Remove");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            StringBuilder errors = new StringBuilder();
            @Override
            public void handle(ActionEvent e) {

                // Get selected object and remove from table
                Classes selectedItem = table.getSelectionModel().getSelectedItem();
                table.getItems().remove(selectedItem);

                // Delete from database
                deleteFromClasses(connection, selectedItem.studentID);

               // Reload information
                readClassesList(connection);
                table.refresh();

                // Update PieChart
                MyPieChart pieChart = new MyPieChart(displayStudentsPerSemesterGPA(connection, "Spring"), total);
                pieChart.drawPieChart(x_width/2, y_height/2, gc);
            }
        });


        Label spacing2 = new Label("");
        spacing.setPrefHeight(25);

        // --------- Remove Button container
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(cbstudentList, delete);
        GridPane.setHgrow(priorityVBox3, Priority.ALWAYS);
        hb2.setPadding(new Insets(10, 10, 10, 20));
        hb2.setSpacing(3);


        // ------ Wrap entire right section
        VBox rightVbox = new VBox();
        rightVbox.setSpacing( 3 );
        GridPane.setVgrow(rightVbox, Priority.ALWAYS);
        rightVbox.getChildren().addAll( studentInfoLabel, hb, spacing, hbButton, spacing2, lblStudentsList, hb2);


        /*
         *  LEFT PORTION OF GUI CONTAINING THE PIE CHART
         */

        // Create pie chart
        myPieChart.drawPieChart(x_width/2, y_height/2, gc);


        /*
         *  DEFINE TOTAL CONSTRAINTS
         */

        gp.add(vboxTable, 0, 1);
        gp.add(canvas, 0, 2);
        gp.add(rightVbox, 1, 2);


        GridPane.setColumnSpan(hb, 6);
        GridPane.setColumnSpan(rightVbox, 6);
        GridPane.setColumnSpan(vboxTable, 1 );
        GridPane.setColumnSpan(canvas, 1 );

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth( 50 );
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth( 50 );
        gp.getColumnConstraints().addAll( col1, col2 );


        // This is outside of the grid pane
        Separator sep5 = new Separator();

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding( new Insets(10) );

        Button saveButton = new Button("Refresh");
        Button cancelButton = new Button("Clear");

        // -------- Refresh the PieChart and Table
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            StringBuilder errors = new StringBuilder();
            @Override
            public void handle(ActionEvent e) {
                // Reload information
                data.clear();
                classes.clear();
                table.refresh();

                readClassesList(connection);
                for(int i=0; i<classes.size(); i++){
                    data.add(classes.get(i));
                }
                table.refresh();

                MyPieChart pieChart = new MyPieChart(displayStudentsPerSemesterGPA(connection, "Spring"), total);
                pieChart.drawPieChart(x_width/2, y_height/2, gc);

            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            StringBuilder errors = new StringBuilder();
            @Override
            public void handle(ActionEvent e) {

            }
        });

        buttonBar.setButtonData(saveButton, ButtonBar.ButtonData.OK_DONE);
        buttonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        buttonBar.getButtons().addAll(saveButton, cancelButton);

        // ---- Surround everything with vbox
        vbox.getChildren().addAll( gp, sep5, buttonBar);

        // Return to scene
        return vbox;
    }







    public static void main(String[] args) { launch(args); }




    // Returns a HashMap with frequency for each GPA
    public static HashMap displayStudentsPerSemesterGPA(Connection connection, String semester){
        String sql = "SELECT studentID, GPA FROM classes WHERE semester = \""+ semester + "\"";
        HashMap<String, Integer> hashMap = new HashMap<>();
        int mtotal = 0;

        try {
             Statement stmt  = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // How many times it occurs and corresponding letter
                String GPA = rs.getString("GPA");
                String studentID = rs.getString("studentID");

                if (hashMap.containsKey(GPA)) {
                    hashMap.put(GPA, hashMap.get(GPA) + 1); // Increment
                } else {
                    hashMap.put(GPA, 1); // Unique
                }
                mtotal++;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        total = mtotal;
        return hashMap;
    }


    // Pass in student and class objects to add student to a class
    public static void insertIntoClasses(Connection connection, Student student, Classes classes) {
        ResultSet rs = null;
        String sql = "INSERT INTO classes(courseID, studentID, section, year, semester, GPA) " + "VALUES(?,?,?,?,?,?)";

        try {
            // Creates the student in the database
            createNewStudent(connection, student);
            // Enters the student into the class
            PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, classes.courseID);
            pstmt.setInt(2, classes.studentID);
            pstmt.setString(3, classes.section);
            pstmt.setInt(4, classes.year);
            pstmt.setString(5, classes.semester);
            pstmt.setString(6, classes.GPA);

            int rowAffected = pstmt.executeUpdate();
            if(rowAffected == 1) {
                rs = pstmt.getGeneratedKeys();
                if(rs.next())
                    System.out.println("Successfully added " + rs.getInt(1));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Removes a student from a class
    public static void deleteFromClasses(Connection connection, int studentID){
        String sql = "DELETE FROM classes " + "WHERE studentID = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, studentID);

            int rowAffected = pstmt.executeUpdate();
            System.out.println(String.format("Row affected %d", rowAffected));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }



    public static void createNewStudent(Connection connection, Student student) {
        ResultSet rs = null;
        String sql = "INSERT INTO students(studentID,firstName,lastName,sex) " + "VALUES(?,?,?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, student.studentID);
            pstmt.setString(2, student.firstName);
            pstmt.setString(3, student.lastName);
            pstmt.setString(4, student.sex);

            int rowAffected = pstmt.executeUpdate();
            if(rowAffected == 1) {
                rs = pstmt.getGeneratedKeys();
                if(rs.next())
                    System.out.println("Successfully added student " + rs.getInt(1));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public static void createNewCourse(Connection connection, Courses course) {
        ResultSet rs = null;
        String sql = "INSERT INTO courses(courseID,courseTitle,department) " + "VALUES(?,?,?)";

        // Using prepared statements before inserting
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, course.courseID);
            pstmt.setString(2, course.courseTitle);
            pstmt.setString(3, course.department);

            int rowAffected = pstmt.executeUpdate();
            if(rowAffected == 1) {
                rs = pstmt.getGeneratedKeys();
                if(rs.next())
                    System.out.println("Successfully added course " + rs.getInt(1));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    // Print all the students
    public static String getStudentName(Connection connection, int studentID){
        String sql = "SELECT * FROM students WHERE studentID = " + studentID;
        String name = "";
        try {
            Statement stmt  = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                name = rs.getString("lastName");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return name;
    }


    // Print all the students
    public static void readStudentsList(Connection connection){
        String sql = "SELECT * FROM students";

        try {
            Statement stmt  = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString("studentID") + "\t" + rs.getString("lastName") +
                        "\t" + rs.getString("firstName") + "\t" + rs.getString("sex"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    // Print all the classes
    public static void readClassesList(Connection connection) {
        String sql = "SELECT * FROM classes";
       ;
        try {
            Statement stmt  = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String lastName = getStudentName(connection,rs.getInt("studentID"));
                Classes classObj = new Classes(
                        rs.getInt("courseID"),
                        rs.getInt("studentID"),
                        rs.getString("section"),
                        rs.getInt("year"),
                        rs.getString("semester"),
                        rs.getString("GPA")
                );
                classObj.setLastName(lastName);
                classes.add(classObj);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    // Print all the courses
    public static void readCoursesList(Connection connection) {
        String sql = "SELECT * FROM courses";

        try {
            Statement stmt  = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString("courseID") + "\t" + rs.getString("courseTitle")
                        + "\t" + rs.getString("department"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    // Allows to update students name and sex. Student id should stay the same
    public static void updateStudent(Connection connection, int studentID, String firstName, String lastName) {
        String sqlUpdate = "UPDATE students " + "SET firstName = ?, lastName = ? " + "WHERE studentID = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlUpdate);
            pstmt.setString(1,firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, studentID);

            int rowAffected = pstmt.executeUpdate();
            System.out.println(String.format("Row affected %d", rowAffected));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Allows to update students name and sex. Student id should stay the same
    public static void updateCourse(Connection connection, int courseID, String courseTitle, String department) {
        String sqlUpdate = "UPDATE courses " + "SET courseTitle = ?, department = ? " + "WHERE courseID = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlUpdate);
            pstmt.setString(1,courseTitle);
            pstmt.setString(2, department);
            pstmt.setInt(3, courseID);

            int rowAffected = pstmt.executeUpdate();
            System.out.println(String.format("Row affected %d", rowAffected));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    // Deletes row from students
    public static void deleteStudentsRow(Connection connection, int studentID){
        String sql = "DELETE FROM students " + "WHERE studentID = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, studentID);

            int rowAffected = pstmt.executeUpdate();
            System.out.println(String.format("Row affected %d", rowAffected));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Deletes row from students
    public static void deleteClassesRow(Connection connection, int studentID, int courseID){
        String sql = "DELETE FROM classes " + "WHERE courseID = ? AND studentID = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, studentID);
            pstmt.setInt(2, courseID);

            int rowAffected = pstmt.executeUpdate();
            System.out.println(String.format("Row affected %d", rowAffected));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    // Deletes row from students
    public static void deleteCoursesRow(Connection connection, int courseID){
        String sql = "DELETE FROM courses " + "WHERE courseID = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, courseID);

            int rowAffected = pstmt.executeUpdate();
            System.out.println(String.format("Row affected %d", rowAffected));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }




}
