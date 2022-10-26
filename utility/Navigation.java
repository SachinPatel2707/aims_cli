package utility;

import view.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class Navigation {
    static HomeView home = new HomeView();
    static FacultyView faculty = new FacultyView();
    static AdminView admin = new AdminView();
    static Login login = new Login();
    static StudentView student = new StudentView();

    public static void navigateTo(String target) throws URISyntaxException, IOException, InterruptedException {
        switch(target)
        {
            case "home":
                home.homeView();
                break;
            case "registerUser":
                login.registerNewUser();
                break;
            case "login":
                login.loginExistingUser();
                break;
            case "studentActions":
                student.initMenu();
                break;
            case "facultyActions":
                faculty.initMenu();
                break;
            case "adminActions":
                admin.initMenu();
                break;
            case "addCatalog":
                admin.addCatalog();
                break;
            case "viewTranscript":
                admin.viewTranscript();
                break;
            case "downloadTranscript":
                admin.downloadTranscript();
                break;
            case "viewStudents":
                student.viewStudentDetails();
                break;
            case "offerCourse":
                faculty.offerCourse();
                break;
            case "uploadMarks":
                faculty.uploadMarks();
                break;
        }
    }
}
