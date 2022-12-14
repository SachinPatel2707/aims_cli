package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;
import utility.Common;
import utility.HttpCalls;
import utility.Navigation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FacultyView {
    static Scanner sc = new Scanner(System.in);
    static Gson gson = new Gson();

    public void offerCourse () throws URISyntaxException, IOException, InterruptedException {
        System.out.println("\n1.View course catalog \n2.Register course \n3.Deregister course \n4.Back");
        int ch = sc.nextInt();
        switch (ch)
        {
            case 1 -> Common.viewCourses();
            case 2 -> registerCourse();
            case 3 -> deregisterCourse();
            case 4 -> Navigation.navigateTo("facultyActions");
        }
    }

    public void uploadMarks () throws IOException, URISyntaxException, InterruptedException {
        System.out.println("Enter the course code");
        String code = sc.next();
        System.out.println("Enter the path of input file");
        String path = sc.next();

        File file = new File(path);
        Scanner fRead = new Scanner(file);
        List<Enrollment> fdata = new ArrayList<>();

        while(fRead.hasNextLine())
        {
            String[] temp = fRead.nextLine().split(",");
            Enrollment en = new Enrollment();
            en.setCourseCode(code);
            en.setsId(temp[0]);
            en.setGrade(Float.parseFloat(temp[1]));
            fdata.add(en);
        }

        HttpCalls.postCall(fdata, "http://localhost:8080/updateGrade");
        Navigation.navigateTo("facultyActions");
    }

    public boolean checkIfRegistered(Course course) throws URISyntaxException, IOException, InterruptedException {
        Course[] response = gson.fromJson(HttpCalls.postCall(course, "http://localhost:8080/getCourse").body(), Course[].class);
        if (response.length == 0 || response[0].getOfferedBy() != null)
        {
            return true;
        }
        return false;
    }

    public void registerCourse () throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Enter the course code");
        Course course = new Course();
        course.setCode(sc.next());
        if(!checkIfRegistered(course)) {
            HttpResponse<String> responsePre = HttpCalls.postCall(course, "http://localhost:8080/getPrerequisites");
            Prerequisite[] preArr = gson.fromJson(responsePre.body(), Prerequisite[].class);

            for (int i = 0; i < preArr.length; i++)
            {
                System.out.println("Enter minimum cgpa requirement for prerequisite " + preArr[i].getPrerequisiteCode());
                preArr[i].setMinCgpa(sc.nextFloat());
            }

            course.setOfferedBy(Data.getUserName());

            HttpCalls.postCall(course, "http://localhost:8080/setOfferedBy");
            HttpCalls.postCall(preArr, "http://localhost:8080/setMinCgpaForPrerequisites");
        }
        else
        {
            System.out.println("Course not found or already registered");
        }
        Navigation.navigateTo("offerCourse");
    }

    public void deregisterCourse() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Enter the course code that you want to deregister");
        Course course = new Course();
        course.setCode(sc.next());

        if(checkIfRegistered(course))
        {
            HttpResponse<String> responsePre = HttpCalls.postCall(course, "http://localhost:8080/getPrerequisites");
            Prerequisite[] preArr = gson.fromJson(responsePre.body(), Prerequisite[].class);
            for (int i = 0; i < preArr.length; i++)
            {
                preArr[i].setMinCgpa(null);
            }

            course.setOfferedBy(null);

            HttpCalls.postCall(course, "http://localhost:8080/setOfferedBy");
            HttpCalls.postCall(preArr, "http://localhost:8080/setMinCgpaForPrerequisites");
        }
        else
        {
            System.out.println("Course not registered");
        }
        Navigation.navigateTo("offerCourse");
    }

    public void initMenu () throws URISyntaxException, IOException, InterruptedException {
        System.out.println("\n1.View student grades \n2.Register/Deregister course \n3.Upload marks \n4.View student list \n5.View students currently enrolled in a course \n6.Update Profile \n7.Logout");
        int ch = sc.nextInt();
        switch (ch) {
            case 1 -> Navigation.navigateTo("viewStudents");
            case 2 -> Navigation.navigateTo("offerCourse");
            case 3 -> Navigation.navigateTo("uploadMarks");
            case 4 -> Navigation.navigateTo("viewStudentsByCourse");
            case 5 -> Navigation.navigateTo("viewCurStudentsByCourse");
            case 6 -> Navigation.navigateTo("updateProfile");
            case 7 -> Navigation.navigateTo("home");
        }
    }
}
