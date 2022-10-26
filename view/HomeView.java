package view;

import model.Data;
import utility.Navigation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class HomeView {
    static Scanner sc = new Scanner (System.in);
    public void homeView () throws URISyntaxException, IOException, InterruptedException {
        Data.resetData();
        System.out.println("\nWelcome to AIMS portal");
        System.out.println("1. Register new User");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        Integer ch = sc.nextInt();

        switch(ch)
        {
            case 1:
                Navigation.navigateTo("registerUser");
                break;
            case 2:
                Navigation.navigateTo("login");
                break;
            case 3:
                return;
        }
    }
}
