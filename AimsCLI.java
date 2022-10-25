import model.Data;
import view.Login;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class AimsCLI
{
    static Scanner sc = new Scanner (System.in);
    public static void main (String[] args) throws URISyntaxException, IOException, InterruptedException {
        boolean flag = true;
        while (flag)
        {
            Scanner sc = new Scanner(System.in);
            Data.resetData();
            System.out.println("Welcome to AIMS portal");
            System.out.println("1. Register new User");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            Integer ch = sc.nextInt();
            Login login = new Login();

            switch(ch)
            {
                case 1:
                    login.registerNewUser();
                    break;
                case 2:
                    login.loginExistingUser();
                    break;
                case 3:
                    return;
            }
        }
    }
}