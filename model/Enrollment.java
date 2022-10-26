package model;

public class Enrollment {
    Integer id;
    String sId;
    String courseCode;
    Integer semester;
    Float grade;

    public Enrollment() {}

    public Enrollment(Integer id, String sId, String courseCode, Integer semester, Float grade) {
        this.id = id;
        this.sId = sId;
        this.courseCode = courseCode;
        this.semester = semester;
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }
}
