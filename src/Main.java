package src;

import src.ui.UI;

public class Main {
    public static void main(String args[]) {
        DBHandler dbhandler = new DBHandler();
<<<<<<< HEAD
=======
        dbhandler.connect("ora_pnguyen6", "a14182869");
        UI ui = new UI();
        ui.showFrame(dbhandler);
>>>>>>> dd2664c4db5146c308804c2115b6ebf070b7a6d3
    }
}


