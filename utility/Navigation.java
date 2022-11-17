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
        switch (target) {
            case "home" -> home.homeView();
            case "registerUser" -> login.registerNewUser();
            case "login" -> login.loginExistingUser();
            case "studentActions" -> student.initMenu();
            case "enroll" -> student.enroll();
            case "unEnroll" -> student.unEnroll();
            case "viewStudents" -> student.viewStudentDetails();
            case "viewStudentsByCourse" -> student.viewStudentsByCourse();
            case "viewCurStudentsByCourse" -> student.viewCurStudentsByCourse();
            case "facultyActions" -> faculty.initMenu();
            case "offerCourse" -> faculty.offerCourse();
            case "uploadMarks" -> faculty.uploadMarks();
            case "adminActions" -> admin.initMenu();
            case "addCatalog" -> admin.addCatalog();
            case "viewTranscript" -> admin.viewTranscript();
            case "downloadTranscript" -> admin.downloadTranscript();
            case "updateProfile" -> Common.updateProfile();
        }
    }
}

// update each profile
