package src;

public class Test {
    public static void main(String[] args) {
        DBHandler dbh = new DBHandler();
        System.out.println("Trying");
        if (dbh.connect("ora_quangv49", "a89911796")) {
            System.out.println("Good");
        } else {
            System.out.println("No good");
            System.exit(0);
        }

        for (StringBuilder s: dbh.problemPlant()) {
            System.out.println(s);
        }

        dbh.close();
    }
}
