package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;
import utility.Common;
import utility.HttpCalls;
import utility.Navigation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.*;

public class StudentView {
    static Scanner sc = new Scanner(System.in);
    static Gson gson = new Gson();

    public boolean validateCourse(String code, Course[] courses) throws URISyntaxException, IOException, InterruptedException {
        boolean flag = false;
        for (Course c : courses)
        {
            if (c.getCode().equals(code))
            {
                flag = true;
                break;
            }
        }
        if (flag)
        {
            Enrollment en = new Enrollment();
            en.setsId(Data.getUserName());
            HttpResponse<String> response = HttpCalls.postCall(en, "http://localhost:8080/getAllEnrollmentByStudent");
            Enrollment[] arr = gson.fromJson(response.body(), Enrollment[].class);
            for (Enrollment enroll : arr)
            {
                if(enroll.getCourseCode().equals(code))
                {
                    System.out.println("Already enrolled to this course");
                    return false;
                }
            }
            return true;
        }
        System.out.println("Incorrect course code");
        return false;
    }

    public boolean validatePrerequisites(User user, String code) throws URISyntaxException, IOException, InterruptedException {
        Course course = new Course();
        course.setCode(code);
        HttpResponse<String> responsePre = HttpCalls.postCall(course, "http://localhost:8080/getPrerequisites");
        Prerequisite[] preArr = gson.fromJson(responsePre.body(), Prerequisite[].class);
        float cgpa = Common.calculateCgpa(user.getUserName(), Data.getCurSem());

        for (Prerequisite pre : preArr)
        {
            Enrollment en = new Enrollment();
            en.setCourseCode(pre.getPrerequisiteCode());
            en.setsId(Data.getUserName());
            HttpResponse<String> response = HttpCalls.postCall(en, "http://localhost:8080/getEnrollment");
            Enrollment[] enArr = gson.fromJson(response.body(), Enrollment[].class);
            if (enArr.length == 0)
                return false;
            else if (enArr[0].getGrade() < pre.getMinCgpa())
                return false;
        }
        return true;
    }

    public void viewStudentDetails () throws URISyntaxException, IOException, InterruptedException {
        String user;
        if (Data.getCategory() == 1)
        {
            user = Data.getUserName();
        }
        else
        {
            System.out.println("Enter StudentId");
            user = sc.next();
        }

        String path = Common.generateTranscript(user);
        Common.readTranscript(path);
        Data.setOutputPath(path);

        String goTo = "";
        if (Data.getCategory() == 1)
            goTo = "studentActions";
        else if (Data.getCategory() == 2)
            goTo = "facultyActions";
        else if (Data.getCategory() == 3)
            return;
        Navigation.navigateTo(goTo);
    }

    public boolean checkCreditLimit(Enrollment en) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> res = HttpCalls.postCall(en, "http://localhost:8080/getAllEnrollmentByStudent");
        Enrollment[] temp = gson.fromJson(res.body(), Enrollment[].class);
        List<Enrollment> arr = new ArrayList<>();
        int sem = en.getSemester();
        if (sem == 1)
        {
            return true;
        }
        else if (sem == 2)
        {
            for (Enrollment e : temp)
            {
                if(e.getSemester() == sem-1)
                    arr.add(e);
            }
        }
        else
        {
            for (Enrollment e : temp)
            {
                if(e.getSemester() == sem-1 || e.getSemester() == sem-2)
                    arr.add(e);
            }
        }
        Map<String, Float> grades = new HashMap<>();
        for (Enrollment e : arr)
        {
            if (e.getGrade() != null)
            {
                grades.put(e.getCourseCode(), e.getGrade());
            }
        }

        Course[] courses = gson.fromJson((HttpCalls.getCall("http://localhost:8080/getAllCourses")).body(), Course[].class);
        Map<String, Float> credits = new HashMap<>();
        Float totalCredits = 0.0f, maxCredit = 0.0f;
        for (Course c : courses)
        {
            credits.put(c.getCode(), c.getCredits().floatValue());
        }

        for(Map.Entry<String, Float> entry : grades.entrySet())
        {
            totalCredits += credits.get(entry.getKey());
        }

        if (sem == 2)
        {
            maxCredit = (totalCredits * (float)1.25);
        }
        else
        {
            maxCredit = (totalCredits / 2) * (float) 1.25;
        }

        arr.clear();
        grades.clear();
        totalCredits = 0.0f;
        for (Enrollment e : temp)
        {
            if(e.getSemester() == sem)
                arr.add(e);
        }
        for (Enrollment e : arr)
        {
            grades.put(e.getCourseCode(), e.getGrade());
        }
        grades.put(en.getCourseCode(), 0.0f);
        for(Map.Entry<String, Float> entry : grades.entrySet())
        {
            totalCredits += credits.get(entry.getKey());
        }

        if (totalCredits > maxCredit)
            return false;
        return true;
    }

    public void enroll() throws URISyntaxException, IOException, InterruptedException {
        Course[] availCourses = Common.viewCurSemCourses();
        User student = new User();
        student.setUserName(Data.getUserName());
        System.out.println("Enter the course code that you want to enroll in");
        Enrollment en = new Enrollment();
        en.setCourseCode(sc.next());
        en.setsId(Data.getUserName());
        en.setSemester(Data.getCurSem());

        if(validateCourse(en.getCourseCode(), availCourses))
        {
            if(validatePrerequisites(student, en.getCourseCode()))
            {
                if (checkCreditLimit(en))
                {
                    HttpCalls.postCall(en, "http://localhost:8080/enroll");
                    System.out.println("Registration successful\n");
                }
                else
                {
                    System.out.println("Registration unsuccessful. Credit limit exceeded.\n");
                }
            }
            else
            {
                System.out.println("Registration unsuccessful. Prerequisites not met.");
            }
        }
        Navigation.navigateTo("studentActions");
    }

    public void unEnroll() throws URISyntaxException, IOException, InterruptedException {
        Enrollment en = new Enrollment();
        en.setsId(Data.getUserName());
        en.setSemester(Data.getCurSem());

        HttpResponse<String> response = HttpCalls.postCall(en, "http://localhost:8080/getEnrollmentByStudentSemester");
        Enrollment[] arr = gson.fromJson(response.body(), Enrollment[].class);

        System.out.println("\nYou are currently enrolled in these courses, select one to unEnroll");
        for (Enrollment enroll : arr)
        {
            System.out.print(enroll.getCourseCode() + " ");
        }
        System.out.println();
        String choice = sc.next();

        boolean flag = false;
        for (Enrollment enroll: arr)
        {
            if (enroll.getCourseCode().equals(choice))
            {
                flag = true;
                en.setCourseCode(choice);
                HttpCalls.postCall(en, "http://localhost:8080/unEnroll");
            }
        }
        if (!flag)
        {
            System.out.println("Incorrect code");
        }
        Navigation.navigateTo("studentActions");
    }

    public void viewStudentsByCourse () throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Enter course code");
        String code = sc.next();
        Course course = new Course();
        course.setCode(code);
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> response = gson.fromJson(HttpCalls.postCall(course, "http://localhost:8080/getEnrollmentByCourse").body(), type);

        User[] allUsers = gson.fromJson(HttpCalls.getCall("http://localhost:8080/getAllUsers").body(), User[].class);

        List<User> selectedUsers = new ArrayList<>();

        for(User user : allUsers)
        {
            if(response.contains(user.getUserName()))
                selectedUsers.add(user);
        }

        Common.printUser(selectedUsers);
        Navigation.navigateTo("facultyActions");
    }

    public void viewCurStudentsByCourse () throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Enter course code");
        String code = sc.next();
        Course course = new Course();
        course.setCode(code);
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> response = gson.fromJson(HttpCalls.postCall(course, "http://localhost:8080/getCurrentEnrollmentByCourse").body(), type);

        User[] allUsers = gson.fromJson(HttpCalls.getCall("http://localhost:8080/getAllUsers").body(), User[].class);

        List<User> selectedUsers = new ArrayList<>();

        for(User user : allUsers)
        {
            if(response.contains(user.getUserName()))
                selectedUsers.add(user);
        }

        Common.printUser(selectedUsers);
        Navigation.navigateTo("facultyActions");
    }

    public void initMenu () throws URISyntaxException, IOException, InterruptedException {
        System.out.println("\n1.View my Grade Sheet \n2.Enroll in course \n3.UnEnroll from course \n4.Update Profile \n5.Logout");
        int ch = sc.nextInt();
        switch (ch) {
            case 1 -> Navigation.navigateTo("viewStudents");
            case 2 -> Navigation.navigateTo("enroll");
            case 3 -> Navigation.navigateTo("unEnroll");
            case 4 -> Navigation.navigateTo("updateProfile");
            case 5 -> Navigation.navigateTo("home");
        }
    }
}
