package src;

import src.ui.UI;

public class Main {
    public static void main(String args[]) {
        DBHandler dbhandler = new DBHandler();
        dbhandler.connect("ora_quangv49", "a89911796");
        UI ui = new UI();
        ui.showFrame(dbhandler);
    }
}


