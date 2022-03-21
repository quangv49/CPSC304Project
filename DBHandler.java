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
     */
    public void addHousehold(String userID, String address, boolean isWaterEnough,
                             boolean isWaterClean, String username, String password,
                             String location) {
        sendCommand(String.format("INSERT INTO UserHousehold VALUES" +
                "(%s, %s, %s, %s, %s, %s, %s)", userID, address, isWaterEnough,
                isWaterClean, username, password, location));
    }

    /**
     * Adds a business.
     */
    public void addBusiness(String userID, String address, boolean isWaterEnough,
                            boolean isWaterClean, String username, String password,
                            String location) {
        sendCommand(String.format("INSERT INTO UserBusiness VALUES" +
                        "(%s, %s, %s, %s, %s, %s, %s)", userID, address, isWaterEnough,
                isWaterClean, username, password, location));
    }

    /**
     * Adds a license. Date format is yyyy-mm-dd.
     */
    public void addLicense(String licenseID, String expiryDate,
                           String dateAuthorized, String userID,
                           boolean isSurface) {
        if (isSurface) {
            sendCommand(String.format("INSERT INTO SurfaceWaterLicense VALUES" +
                            "(%s, %s, %s, %s)", licenseID, expiryDate, dateAuthorized,
                    userID));
        } else {
            sendCommand(String.format("INSERT INTO GroundWaterLicense VALUES" +
                            "(%s, %s, %s, %s)", licenseID, expiryDate, dateAuthorized,
                    userID));
        }
    }

    /**
     * Adds a business.
     */
    public void addBodyOfWater(String waterID, String name, String type,
                               boolean isSurface) {
        sendCommand(String.format("INSERT INTO BodyOfWater VALUES" +
                "(%s, %s, %s)", waterID, name, type));
        if (isSurface) {
            sendCommand(String.format("INSERT INTO SurfaceWater VALUES" +
                            "(%s)", waterID));
        } else {
            sendCommand(String.format("INSERT INTO GroundWater VALUES" +
                    "(%s)", waterID));
        }
    }

    /**
     * Deletes a household.
     */
    public void deleteHousehold(String userID) {
        sendCommand(String.format("DELETE FROM UserHousehold WHERE userID = %s",
                                    userID));
    }

    /**
     * Deletes a business.
     */
    public void deleteBusiness(String userID) {
        sendCommand(String.format("DELETE FROM UserBusiness WHERE userID = %s",
                                    userID));
    }

    /**
     * Updates the address of a household or a business.
     */
    public void updateAddress(String userID, String address,
                              boolean isHousehold) {
        if (isHousehold) {
            sendCommand(String.format("UPDATE UserHousehold SET address = %s " +
                    "WHERE userID = %s", address, userID));
        } else {
            sendCommand(String.format("UPDATE UserBusiness SET address = %s " +
                    "WHERE userID = %s", address, userID));
        }
    }

    /**
     * Returns list of users in a location.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> usersInLocation(String location) {
        return sendCommand(String.format("SELECT userID FROM UserHousehold WHERE location = %s " +
                "UNION SELECT userID FROM UserBusiness WHERE location = %s", location, location));
    }

    /**
     * Returns list of users with the specified water quality.
     */
    public ArrayList<StringBuilder> usersWithWaterQuality(boolean isWaterEnough, boolean isWaterClean) {
        return sendCommand(String.format("SELECT userID FROM UserHousehold WHERE isWaterEnough = %s AND isWaterClean = %s " +
                "UNION SELECT userID FROM UserBusiness WHERE isWaterEnough = %s AND isWaterClean = %s",
                isWaterEnough, isWaterClean, isWaterEnough, isWaterClean));
    }

    /**
     * Returns list of users and their report on water quality.
     */
    public ArrayList<StringBuilder> waterQualityInfo() {
        return sendCommand("SELECT userID, isWaterEnough, isWaterClean FROM UserHousehold " +
                        "UNION SELECT userID, isWaterEnough, isWaterClean FROM Business");
    }

    /**
     * Returns licenses and water sources they draw from.
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
