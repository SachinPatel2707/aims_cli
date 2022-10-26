package model;

public class User {
    private Integer id;
    private String userName;
    private String firstName;
    private String lastName;
    private Integer category;
    private String password;
    private String yearOfEnrollment;
    private String Department;
    private Float cgpa;
    private Float totalCreditsEarned;

    public User () {}

    public User(String userName, String firstName, String lastName, Integer category, String password) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.category = category;
        this.password = password;
    }

    public User(Integer id, String userName, String firstName, String lastName, Integer category, String password, String yearOfEnrollment, String department, Float cgpa, Float totalCreditsEarned) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.category = category;
        this.password = password;
        this.yearOfEnrollment = yearOfEnrollment;
        Department = department;
        this.cgpa = cgpa;
        this.totalCreditsEarned = totalCreditsEarned;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYearOfEnrollment() {
        return yearOfEnrollment;
    }

    public void setYearOfEnrollment(String yearOfEnrollment) {
        this.yearOfEnrollment = yearOfEnrollment;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public Float getCgpa() {
        return cgpa;
    }

    public void setCgpa(Float cgpa) {
        this.cgpa = cgpa;
    }

    public Float getTotalCreditsEarned() {
        return totalCreditsEarned;
    }

    public void setTotalCreditsEarned(Float totalCreditsEarned) {
        this.totalCreditsEarned = totalCreditsEarned;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
