import java.sql.*;

public class DBHandler {
    private static final String url = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    private Connection connection = null;

    /**
     * Initializes a DBHandler object.
     */
    public DBHandler() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Connects to database.
     * @param user Username with ora.
     * @param password Student number version.
     * @return True if connects successfully, false otherwise.
     */
    public boolean connect(String user, String password) {
        try {
            if (connection != null) connection.close();

            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);

            System.out.println("Connected.");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Closes session if object no longer used.
     */
    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void rollback() {
        try {
            if (connection != null) connection.rollback();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Generic function which sends a SQL command.
     *
     * @param command Processed SQL statement as a string (String.format() is expected).
     * @param s A Statement from connection.createStatement() should be provided so that it's closeable later.
     * @return Null if there is an error or the SQL statement doesn't return anything, or a ResultSet
     * if the statement is a query.
     */
    public ResultSet sendCommand(String command, Statement s) {
        try {
            if (command.contains("INSERT") || command.contains("DELETE") || command.contains("UPDATE")) {
                s.executeUpdate(command);
                s.close();
                return null;
            } else {
                return s.executeQuery(command);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
