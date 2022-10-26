package utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.*;

public class Common {
    static Gson gson = new Gson();

    public static void printCourses (Course[] courses)
    {
        System.out.format("%-15s%-30s%-10s%-20s%-50s\n", "CourseCode ", "Course name ", "l-t-p-c ", "offered by ", "prerequisites");
        for (Course c : courses)
        {
            System.out.format("%-15s%-30s%-10s%-20s", c.getCode(), c.getName(), c.getL() + "-" + c.getT() + "-" + c.getP() + "-" + c.getCredits(), c.getOfferedBy());
            for (String s : c.getPreReq())
            {
                System.out.format("%-5s", s);
            }
            System.out.println();
        }
    }

    public static void viewCourses () throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = HttpCalls.getCall("http://localhost:8080/getAllCourses");
        Course[] courses = gson.fromJson(response.body(), Course[].class);

        for (Course course : courses)
        {
            HttpResponse<String> responsePre = HttpCalls.postCall(course, "http://localhost:8080/getPrerequisites");
            Type preType = new TypeToken<List<Prerequisite>>(){}.getType();
            List<Prerequisite> preArr = gson.fromJson(responsePre.body(), preType);

            List<String> temp = new ArrayList<>();
            for (Prerequisite p : preArr)
            {
                temp.add(p.getPrerequisiteCode());
            }
            course.setPreReq(temp);
        }
        printCourses(courses);
        Navigation.navigateTo("offerCourse");
    }

    public static Course[] viewCurSemCourses() throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = HttpCalls.getCall("http://localhost:8080/getCurrentSemCourses");
        Course[] courses = gson.fromJson(response.body(), Course[].class);

        for (Course course : courses)
        {
            HttpResponse<String> responsePre = HttpCalls.postCall(course, "http://localhost:8080/getPrerequisites");
            Type preType = new TypeToken<List<Prerequisite>>(){}.getType();
            List<Prerequisite> preArr = gson.fromJson(responsePre.body(), preType);

            List<String> temp = new ArrayList<>();
            for (Prerequisite p : preArr)
            {
                temp.add(p.getPrerequisiteCode());
            }
            course.setPreReq(temp);
        }
        printCourses(courses);
        return courses;
    }

    public static float calculateGpa(String user, Integer sem) throws URISyntaxException, IOException, InterruptedException {
        if (sem == Data.getCurSem())
            return 0.0f;
        Enrollment en = new Enrollment();
        en.setsId(user);
        en.setSemester(sem);

        HttpResponse<String> res = HttpCalls.postCall(en, "http://localhost:8080/getEnrollmentByStudentSemester");
        Enrollment[] arr = gson.fromJson(res.body(), Enrollment[].class);
        Map<String, Float> grades = new HashMap<>();
        for (Enrollment e : arr)
        {
            grades.put(e.getCourseCode(), e.getGrade());
        }

        Course[] courses = gson.fromJson((HttpCalls.getCall("http://localhost:8080/getAllCourses")).body(), Course[].class);
        Map<String, Float> credits = new HashMap<>();
        for (Course c : courses)
        {
            credits.put(c.getCode(), c.getCredits().floatValue());
        }

        float obtainedGrades = 0.0f;
        for(Map.Entry<String, Float> entry : grades.entrySet())
        {
            obtainedGrades += (entry.getValue() * credits.get(entry.getKey()));
        }
        float totalGrades = 0.0f;
        for(Map.Entry<String, Float> entry : grades.entrySet())
        {
            totalGrades += (10 * credits.get(entry.getKey()));
        }

        float gpa = obtainedGrades * 10;
        gpa = gpa / totalGrades;
        return gpa;
    }

    public static float calculateCgpa(String user, Integer sem) throws URISyntaxException, IOException, InterruptedException {
        if (sem == Data.getCurSem())
            sem--;
        Enrollment en = new Enrollment();
        en.setsId(user);

        HttpResponse<String> res = HttpCalls.postCall(en, "http://localhost:8080/getAllEnrollmentByStudent");
        Enrollment[] temp = gson.fromJson(res.body(), Enrollment[].class);
        List<Enrollment> arr = new ArrayList<>();
        for (Enrollment e : temp)
        {
            if(e.getSemester() <= sem)
                arr.add(e);
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
        Float totalCredits = 0.0f;
        for (Course c : courses)
        {
            credits.put(c.getCode(), c.getCredits().floatValue());
        }

        float obtainedGrades = 0.0f;
        for(Map.Entry<String, Float> entry : grades.entrySet())
        {
            obtainedGrades += (entry.getValue() * credits.get(entry.getKey()));
        }
        float totalGrades = 0.0f;
        for(Map.Entry<String, Float> entry : grades.entrySet())
        {
            totalGrades += (10 * credits.get(entry.getKey()));
            totalCredits += credits.get(entry.getKey());
        }

        float cgpa = obtainedGrades * 10;
        cgpa = cgpa / totalGrades;

        User u = new User();
        u.setUserName(user);
        u = gson.fromJson(HttpCalls.postCall(u, "http://localhost:8080/getUser").body(), User.class);
        u.setCgpa(cgpa);
        u.setTotalCreditsEarned(totalCredits);

        HttpCalls.postCall(u, "http://localhost:8080/updateUser");
        return cgpa;
    }

    public static void printUser(List<User> users)
    {
        System.out.format("%-20s%-15s%-15s%-15s%-15s%-15s\n", "sId ", "firstName ", "lastName ", "department ", "cgpa ", "totalCredits");
        for (User user : users)
        {
            System.out.format("%-20s%-15s%-15s%-15s%-15f%-15f\n", user.getUserName(), user.getFirstName(), user.getLastName(), user.getDepartment(), user.getCgpa(), user.getTotalCreditsEarned());
        }
    }

    public static String generateTranscript(String id) throws IOException, URISyntaxException, InterruptedException {
        User u = new User();
        u.setUserName(id);
        User user = gson.fromJson(HttpCalls.postCall(u, "http://localhost:8080/getUser").body(), User.class);

        Enrollment enroll = new Enrollment();
        enroll.setsId(id);
        List<Enrollment[]> enrollments = new ArrayList<>();

        calculateCgpa(id, Data.getCurSem());

        for (int i = 1; i <= Data.getCurSem(); i++)
        {
            enroll.setSemester(i);
            Enrollment[] enrollment = gson.fromJson(HttpCalls.postCall(enroll, "http://localhost:8080/getEnrollmentByStudentSemester").body(), Enrollment[].class);
            enrollments.add(enrollment);
        }

        Course[] courses = gson.fromJson(HttpCalls.getCall("http://localhost:8080/getAllCourses").body(), Course[].class);
        Map<String, String> courseNames = new HashMap<>();
        Map<String, Integer> courseCredits = new HashMap<>();

        for (Course course : courses)
        {
            courseNames.put(course.getCode(), course.getName());
            courseCredits.put(course.getCode(), course.getCredits());
        }

        String path = "output/" + id + ".txt";
        File original = new File(path);
        original.createNewFile();
        if (original.exists())
        {
            FileWriter fwrite = new FileWriter(original);
            int count = 0;
            int credit = 0;
            String[] str = new String[200];
            str[count++] = "\n";
            str[count++] = String.format("%-20s%-15s\n%-20s%-15s\n%-20s%-15s\n%-20s%-15s\n%-20s%-15s\n%-20s%-15s\n\n", "College", "IIT ROPAR", "StudentId", user.getUserName(), "FirstName", user.getFirstName(), "LastName", user.getLastName(), "Department", user.getDepartment(), "Enrollment Year", user.getYearOfEnrollment(), "CGPA", user.getCgpa(), "Total Credits", user.getTotalCreditsEarned());

            str[count++] = String.format("%-20s%-20s%-30s%-20s%-20s\n", "S.No", "CourseCode", "CourseName", "Grade", "Credits");

            for (int sem = 1; sem <= enrollments.size(); sem++)
            {
                str[count++] = String.format("%-110s", "-----------------------------------------------------------------------------------------------------------");
                str[count++] = String.format("%-20s%-20s%-30s%-20s%-20s\n", "", "", "Semester " + sem, " ", " ");
                str[count++] = String.format("%-110s\n", "-----------------------------------------------------------------------------------------------------------");
                int totalcredit = 0;
                for (int i = 0; i < enrollments.get(sem-1).length; i++)
                {
                    Enrollment en = enrollments.get(sem-1)[i];
                    totalcredit += courseCredits.get(en.getCourseCode());
                    str[count++] = String.format("%-20s%-20s%-30s%-20s%-20s\n", i+1, en.getCourseCode(), courseNames.get(en.getCourseCode()), en.getGrade()+"", courseCredits.get(en.getCourseCode())+"");
                }
                float gpa = calculateGpa(id, sem);
                float cgpa = calculateCgpa(id, sem);
                credit += totalcredit;

                if (sem == Data.getCurSem())
                {
                    credit -= totalcredit;
                    totalcredit = 0;
                }
                str[count++] = String.format("%-20s%-20s%30s%-20s%-20s\n", "", "", "SGPA ", gpa+"", totalcredit+"");
                str[count++] = String.format("%-20s%-20s%30s%-20s%-20s\n", "", "", "CGPA ", cgpa+"", credit+"");
                str[count++] = "\n";
            }
            str[count++] = String.format("%-110s", "-----------------------------------------------------------------------------------------------------------");

            for (int i = 0; i < count; i++)
            {
                fwrite.write(str[i]);
            }
            fwrite.close();
        }
        return path;
    }

    public static void readTranscript(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner fRead = new Scanner(file);
        List<String> fReadData = new ArrayList<>();

        System.out.println();
        while (fRead.hasNextLine()) {
//          fReadData.add(fRead.nextLine());
            System.out.println(fRead.nextLine());
        }
        fRead.close();
    }
}
