import java.util.Random;

public class Student {
    String firstName, lastName, sex;
    int studentID;

    public Student(String first, String last, String s){
        this.firstName = first;
        this.lastName = last;
        this.sex = s;
        this.studentID = generateID();
    }

    public int generateID(){
        Random random = new Random();
        return Integer.parseInt(String.format("%04d", random.nextInt(10000)));
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSex() {
        return sex;
    }

    public int getStudentID() {
        return studentID;
    }
}
