package model;

public class Prerequisite {
    Integer id;
    String courseCode;
    String prerequisiteCode;
    Float minCgpa;

    public Prerequisite () {}

    public Prerequisite(String courseCode, String prerequisiteCode) {
        this.courseCode = courseCode;
        this.prerequisiteCode = prerequisiteCode;
    }

    public Prerequisite(Integer id, String courseCode, String prerequisiteCode) {
        this.id = id;
        this.courseCode = courseCode;
        this.prerequisiteCode = prerequisiteCode;
    }

    public Float getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(Float minCgpa) {
        this.minCgpa = minCgpa;
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
