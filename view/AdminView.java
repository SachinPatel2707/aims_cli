package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Course;
import model.Data;
import model.Prerequisite;
import utility.Common;
import utility.HttpCalls;
import utility.Navigation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class AdminView {
    static Scanner sc = new Scanner(System.in);
    static Gson gson = new Gson();

    private static void addPrerequisite(String code, String[] pre) throws URISyntaxException, IOException, InterruptedException {
        for (String preCode : pre)
        {
            Prerequisite prerequisite = new Prerequisite(code, preCode);
            Course course = new Course();
            course.setCode(preCode);
            HttpResponse<String> httpResponse =  HttpCalls.postCall(course, "http://localhost:8080/getCourse");
            Type courseType = new TypeToken<List<Course>>(){}.getType();
            List<Course> temp = gson.fromJson(httpResponse.body(), courseType);
            if (temp.size() > 0)
            {
                httpResponse = HttpCalls.postCall(prerequisite, "http://localhost:8080/addPrerequisite");
            }
            else
            {
                System.out.println(preCode + " course does not exist");
            }
        }
    }

    public void addCatalog() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Enter course code");
        String code = sc.next();
        System.out.println("Enter course name");
        String name = sc.next();
        System.out.println("Enter course credits");
        Integer credits = sc.nextInt();
        System.out.println("Enter number of lectures");
        Integer l = sc.nextInt();
        System.out.println("Enter number of tutorials");
        Integer t = sc.nextInt();
        System.out.println("Enter number of practicals");
        Integer p = sc.nextInt();

        System.out.println("Enter the number of prerequisites this course has");
        Integer n = sc.nextInt();
        sc.nextLine();
        String[] pre = new String[n];
        for (int i = 0; i < n; i++)
        {
            pre[i] = sc.nextLine();
        }

        Course course = new Course(code, name, credits, l, t, p);
        HttpResponse<String> postResponse = HttpCalls.postCall(course, "http://localhost:8080/addCourse");

        addPrerequisite(code, pre);

        if (postResponse.statusCode() == 200)
        {
            System.out.println("Course added to catalog successfully\n");
        }
        Navigation.navigateTo("adminActions");
    }

    public void viewTranscript () throws URISyntaxException, IOException, InterruptedException {
        Navigation.navigateTo("viewStudents");
        Navigation.navigateTo("adminActions");
    }

    public void downloadTranscript () throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Enter StudentId");
        String user = sc.next();
        Data.setOutputPath(Common.generateTranscript(user));
        System.out.println("\nTranscript is in file : " + Data.getOutputPath());
        Navigation.navigateTo("adminActions");
    }

    public void initMenu () throws URISyntaxException, IOException, InterruptedException {
        System.out.println("\n1.Add a course to catalog \n2.View transcript \n3.Download transcript \n4.Logout");
        int ch = sc.nextInt();
        switch (ch) {
            case 1 -> Navigation.navigateTo("addCatalog");
            case 2 -> Navigation.navigateTo("viewTranscript");
            case 3 -> Navigation.navigateTo("downloadTranscript");
            case 4 -> Navigation.navigateTo("home");
        }
    }
}
