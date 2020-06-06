public class Classes {
    String semester, section, GPA, lastName;
    int courseID, studentID, year;

    public Classes(int courseID, int studentID, String section, int year, String semester, String GPA){
        this.courseID = courseID;
        this.studentID = studentID;
        this.section = section;
        this.year = year;
        this.semester = semester;
        this.GPA = GPA;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getSemester() {
        return semester;
    }

    public String getSection() {
        return section;
    }

    public String getGPA() {
        return GPA;
    }

    public int getCourseID() {
        return courseID;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getYear() {
        return year;
    }
}
