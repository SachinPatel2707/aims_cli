package model;

public class User {
    private Integer id;
    private String userName;
    private String firstName;
    private String lastName;
    private Integer category;
    private String password;

    public User () {}

    public User (String userName, String firstName, String lastName, Integer category, String password) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.category = category;
        this.password = password;
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
