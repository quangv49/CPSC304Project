package src;

import src.ui.UI;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        DBHandler dbhandler = new DBHandler();
        Scanner in = new Scanner(System.in);
        String username, password;

        do {
            System.out.println("Username:");
            username = in.nextLine();
            System.out.println("Password:");
            password = in.nextLine();
        } while (!dbhandler.connect(username, password));

        UI ui = new UI();
        ui.showFrame(dbhandler);
    }
}


