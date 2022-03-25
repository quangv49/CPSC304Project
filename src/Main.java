package src;

import src.ui.UI;

public class Main {
    public static void main(String args[]) {
        DBHandler dbhandler = new DBHandler();
        dbhandler.connect("ora_pnguyen6", "a14182869");
        UI ui = new UI();
        ui.showFrame();
    }
}


