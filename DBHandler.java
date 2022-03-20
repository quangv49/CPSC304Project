import java.sql.*;
import java.util.ArrayList;

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

    /**
     * Does what Connection.rollback() does for the connection attribute.
     */
    public void rollback() {
        try {
            if (connection != null) connection.rollback();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Generic function which sends a SQL command and returns the result as an ArrayList<String>.
     *
     * @param command Processed SQL statement as a string (String.format() is expected).
     * @return Null if there is an error or the SQL statement doesn't return anything, or an ArrayList<StringBuilder>
     * if the statement is a query.
     */
    public ArrayList<StringBuilder> sendCommand(String command) {
        try {
            Statement s = connection.createStatement();
            if (command.contains("INSERT") || command.contains("DELETE") || command.contains("UPDATE")) {
                s.executeUpdate(command);
                connection.commit();
                s.close();
                return null;
            } else {
                ResultSet set = s.executeQuery(command);
                ResultSetMetaData meta = set.getMetaData();
                ArrayList<StringBuilder> result = new ArrayList<>();

                int numCol = meta.getColumnCount();
                StringBuilder temp = new StringBuilder();

                for (int i = 0; i <= numCol; i++) {
                    temp.append(String.format("%-20s", meta.getColumnLabel(i)));
                }

                while (set.next()) {
                    temp = new StringBuilder();
                    for (int i = 0; i <= numCol; i++) {
                        temp.append(String.format("%-20s", set.getString(meta.getColumnLabel(i))));
                    }
                    result.add(temp);
                }

                set.close();
                s.close();
                return result;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Adds a household.
     *
     * @param attributesGoHere Placeholder.
     */
    public void addHousehold(String attributesGoHere) {
        sendCommand(attributesGoHere);
    }

    /**
     * Adds a busines.
     *
     * @param attributesGoHere Placeholder.
     */
    public void addBusiness(String attributesGoHere) {
        sendCommand(attributesGoHere);
    }

    /**
     * Adds a license.
     *
     * @param attributesGoHere Placeholder.
     */
    public void addLicense(String attributesGoHere) {
        sendCommand(attributesGoHere);
    }

    /**
     * Deletes a household.
     *
     * @param attributesGoHere Placeholder.
     */
    public void deleteHousehold(String attributesGoHere) {
        sendCommand(attributesGoHere);
    }

    /**
     * Deletes a business.
     *
     * @param attributesGoHere Placeholder.
     */
    public void deleteBusiness(String attributesGoHere) {
        sendCommand(attributesGoHere);
    }

    /**
     * Updates the address of a household or a business.
     *
     * @param attributesGoHere Placeholder.
     * @param isHousehold Specifies if a household's address is to be updated.
     */
    public void updateAddress(String attributesGoHere, boolean isHousehold) {
        sendCommand(attributesGoHere);
    }

    /**
     * Returns list of users in a location.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> usersInLocation(String attributesGoHere) {
        return sendCommand(attributesGoHere);
    }

    /**
     * Returns list of users with either not enough or dirty water.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> usersWithBadWater(String attributesGoHere) {
        return sendCommand(attributesGoHere);
    }

    /**
     * Returns list of users and their report on water quality.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> waterQualityInfo(String attributesGoHere) {
        return sendCommand(attributesGoHere);
    }

    /**
     * Returns licenses and water sources they draw from.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> licenseSource(String attributesGoHere) {
        return sendCommand(attributesGoHere);
    }

    /**
     * Returns number of users with unclean or not enough water.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> numUsersWithBadWater(String attributesGoHere) {
        return sendCommand(attributesGoHere);
    }

    /**
     * Returns water source that is used by the highest number of businesses.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> stressedWaterSource(String attributesGoHere) {
        return sendCommand(attributesGoHere);
    }

    /**
     * Returns the user that holds all licenses drawing from a water source.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> monoUser(String attributesGoHere) {
        return sendCommand(attributesGoHere);
    }

    /**
     * Returns sewage plants that handle all locations with at least one user with bad water.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> problemPlant(String attributesGoHere) {
        return sendCommand(attributesGoHere);
    }
}
