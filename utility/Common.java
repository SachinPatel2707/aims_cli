package utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Course;
import model.Prerequisite;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Common {
    static Gson gson = new Gson();

    private static void printCourses (List<Course> courses)
    {
        System.out.println("CourseCode | Course name | l-t-p-c | offered by | prerequisites");
        for (Course c : courses)
        {
            System.out.print(c.getCode() + " | " + c.getName() + " | " + c.getL() + "-" + c.getT() + "-" + c.getP() + "-" + c.getCredits() + " | " + c.getOfferedBy() + " | ");
            for (String s : c.getPreReq())
            {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

    public static void viewCourses () throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = HttpCalls.getCall("http://localhost:8080/getAllCourses");
        Type courseType = new TypeToken<List<Course>>(){}.getType();
        List<Course> courses = gson.fromJson(response.body(), courseType);

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
}
