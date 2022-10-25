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

public class Login
{
    static Scanner sc = new Scanner(System.in);

    public void loginExistingUser () throws URISyntaxException, IOException, InterruptedException {
        String userName, password;
        System.out.println("Enter username");
        userName = sc.nextLine();
        System.out.println("Enter password");
        password = sc.nextLine();

        User user = new User();
        user.setUserName(userName);

        Gson gson = new Gson();
        String postBody = gson.toJson(user);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/checkUser"))
                .setHeader("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postBody))
                .build();

        HttpClient http = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = http.send(postRequest, HttpResponse.BodyHandlers.ofString());
        User resUser = gson.fromJson(postResponse.body(), User.class);

        if (resUser == null)
        {
            System.out.println("User not found");
        }
        else if (!resUser.getPassword().equals(password))
        {
            System.out.println("Incorrect password");
        }
        else
        {
            Data.setUserName(resUser.getUserName());
            Data.setCategory(resUser.getCategory());
            switch(resUser.getCategory())
            {
                case 1:
                    StudentView.initMenu();
                    break;
                case 2:
                    FacultyView.initMenu();
                    break;
                case 3:
                    AdminView.initMenu();
                    break;
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
        Gson gson = new Gson();
        String postBody = gson.toJson(newUser);

//        System.out.println(postBody);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/registerUser"))
                .setHeader("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postBody))
                .build();

        HttpClient http = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = http.send(postRequest, HttpResponse.BodyHandlers.ofString());

        if (postResponse.statusCode() == 200)
        {
            System.out.println("Registration successful\n");
        }
    }
}