package view;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import com.google.gson.Gson;
import model.Data;
import model.User;
import utility.HttpCalls;
import utility.Navigation;

public class Login
{
    static Scanner sc = new Scanner(System.in);
    Gson gson = new Gson();

    public void loginExistingUser () throws URISyntaxException, IOException, InterruptedException {
        String userName, password;
        System.out.println("Enter username");
        userName = sc.nextLine();
        System.out.println("Enter password");
        password = sc.nextLine();

        User user = new User();
        user.setUserName(userName);

        HttpResponse<String> postResponse = HttpCalls.postCall(user, "http://localhost:8080/checkUser");
        User resUser = gson.fromJson(postResponse.body(), User.class);

        if (resUser == null)
        {
            System.out.println("User not found");
            Navigation.navigateTo("home");
        }
        else if (!resUser.getPassword().equals(password))
        {
            System.out.println("Incorrect password");
            Navigation.navigateTo("home");
        }
        else
        {
            Data.setUserName(resUser.getUserName());
            Data.setCategory(resUser.getCategory());
            switch (resUser.getCategory()) {
                case 1 -> Navigation.navigateTo("studentActions");
                case 2 -> Navigation.navigateTo("facultyActions");
                case 3 -> Navigation.navigateTo("adminActions");
            }
        }
    }

    public void registerNewUser () throws URISyntaxException, IOException, InterruptedException {
        String userName, firstName, lastName, password;
        Integer category;

        System.out.println("Select category\n1.Student \n2.Faculty \n3.Academics Office\n");
        category = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter first name");
        firstName = sc.nextLine();
        System.out.println("Enter last name");
        lastName = sc.nextLine();
        System.out.println("Enter user name");
        userName = sc.nextLine();
        System.out.println("Enter password (at least 4 length)");
        password = sc.nextLine();

        User newUser = new User(userName, firstName, lastName, category, password);
        HttpResponse<String> postResponse = HttpCalls.postCall(newUser, "http://localhost:8080/registerUser");

        if (postResponse.statusCode() == 200)
        {
            System.out.println("Registration successful\n");
        }
        Navigation.navigateTo("home");
    }
}