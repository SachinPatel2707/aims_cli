package model;

public class Prerequisite {
    Integer id;
    String courseCode;
    String prerequisiteCode;

    public Prerequisite () {}

    public Prerequisite(String courseCode, String prerequisiteCode) {
        this.courseCode = courseCode;
        this.prerequisiteCode = prerequisiteCode;
    }

    public Integer getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getPrerequisiteCode() {
        return prerequisiteCode;
    }

    public void setPrerequisiteCode(String prerequisiteCode) {
        this.prerequisiteCode = prerequisiteCode;
    }
}
