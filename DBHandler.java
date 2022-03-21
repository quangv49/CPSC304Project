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
     */
    /*public ArrayList<StringBuilder> usersInLocation(String location) {
        return sendCommand(String.format("SELECT userID FROM UserHousehold WHERE location = %s " +
                "UNION SELECT userID FROM UserBusiness WHERE location = %s", location, location));
    }*/

    /**
     * Returns list of users with the specified water quality.
     */
    /*public ArrayList<StringBuilder> usersWithWaterQuality(boolean isWaterEnough, boolean isWaterClean) {
        return sendCommand(String.format("SELECT userID FROM UserHousehold WHERE isWaterEnough = %s AND isWaterClean = %s " +
                "UNION SELECT userID FROM UserBusiness WHERE isWaterEnough = %s AND isWaterClean = %s",
                isWaterEnough, isWaterClean, isWaterEnough, isWaterClean));
    }*/

    /**
     * Returns tuples with user-specified fields from a particular table satisfying one condition.
     */
    public ArrayList<StringBuilder> select(String fields, String table, String condition) {
        return sendCommand(String.format("SELECT %s FROM %s WHERE %s", fields, table, condition));
    }

    /**
     * Returns list of users and a particular field.
     */
    public ArrayList<StringBuilder> project(String fields, String table) {
        return sendCommand(String.format("SELECT %s FROM %table", fields, table));
    }

    /**
     * Returns licenses and water sources they draw from.
     */
    public ArrayList<StringBuilder> licenseSource() {
        return sendCommand("SELECT userID, licenseID, waterID, name FROM UserBusiness U, GroundWaterLicense GWL, DrawsGround DG, BodyOfWater BOW" +
                " WHERE U.userID = GWL.userID AND  GWL.licenseID = DG.licenseID AND BOW.waterID = DG.waterID" +
                " UNION SELECT userID, licenseID, waterID, name FROM UserBusiness U, SurfaceWaterLicense SWL, DrawsSurface DS, BodyOfWater BOW" +
                " WHERE U.userID = SWL.userID AND SWL.licenseID = DS.licenseID AND BOW.waterID = DS.waterID");
    }

    /**
     * Returns a list of names of dams and names of rivers upstream and downstream to each dam
     */
    public ArrayList<StringBuilder> damInfo() {
        return sendCommand("SELECT D.name, B1.name AS upstreamName, B2.name AS downstreamName FROM Dams D, BodyOfWater B1, BodyOfWater B2" +
                " WHERE D.upstream = B1.waterID AND D.downstream = B2.waterID");
    }

    /**
     * Returns all measurements made by stations measuring a particular water source.
     */
    public ArrayList<StringBuilder> sourceMeasurements(String waterID) {
        return sendCommand("SELECT B.name, S.stationID, SM.variable, SM.time, SM.Value, VU.unit "+
                "FROM BodyOfWater B, Stations S, StationMeasurements SM, VariableUnits VU "+
                "WHERE B.waterID = S.waterID AND S.stationID = SM.stationID AND SM.variable = VU.variable AND B.waterID = " + waterID);
    }

    /**
     * Generic join method.
     */
    @Deprecated
    public ArrayList<StringBuilder> join(String table1, String table2) {
        try {
            DatabaseMetaData d = connection.getMetaData();
            ResultSet foreignKeys = d.getImportedKeys(null,null,table1);
            ResultSet primaryKey = d.getPrimaryKeys(null,null,table2);

            String currentForeignKey = null;
            primaryKey.next();
            String currentPrimaryKey = primaryKey.getString("COLUMN_NAME");

            while (foreignKeys.next()) {
                if (currentPrimaryKey == foreignKeys.getString("FK_NAME")) {
                    currentForeignKey = foreignKeys.getString("FK_NAME");
                    break;
                }
            }

            if (currentForeignKey != null) {
                return sendCommand(String.format("SELECT * FROM %s, %s WHERE " +
                        "%s.%s = %s.%s", table1, table2, table1, currentForeignKey,
                        table2, currentPrimaryKey));
            } else {
                foreignKeys = d.getImportedKeys(null,null,table2);
                primaryKey = d.getPrimaryKeys(null,null,table1);

                currentForeignKey = null;
                primaryKey.next();
                currentPrimaryKey = primaryKey.getString("COLUMN_NAME");

                while (foreignKeys.next()) {
                    if (currentPrimaryKey == foreignKeys.getString("FK_NAME")) {
                        currentForeignKey = foreignKeys.getString("FK_NAME");
                        break;
                    }
                }

                return sendCommand(String.format("SELECT * FROM %s, %s WHERE " +
                                "%s.%s = %s.%s", table1, table2, table2, currentForeignKey,
                        table1, currentPrimaryKey));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Returns number of users with unclean or not enough water.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> numUsersWithBadWater() {
        return sendCommand("SELECT COUNT(userID) FROM UserBusiness WHERE isWaterEnough = False AND isWaterClean = False");
    }

    /**
     * Returns water source that is used by the highest number of businesses.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> stressedWaterSource(String attributesGoHere) {
        return sendCommand("WITH Temp AS " +
                "    SELECT GW.waterID as waterID, COUNT(distinct U.userID) as numUser " +
                "    FROM UserBusiness U, GroundWaterLicense GL, DrawsGround DG, GroundWater GW " +
                "    Where U.userID = GL.userID " +
                "          AND  GL.waterID = DG.waterID " +
                "          AND   GW.waterID = DG.waterID " +
                "    GROUP BY GW.waterID " +
                "SELECT waterID " +
                "FROM Temp " +
                "WHERE numUser = (SELECT MAX(numUser) From Temp)");
    }

    /**
     * Returns the user that holds all licenses drawing from a water source.
     */
    public ArrayList<StringBuilder> monoUser(String waterID, String dateAuthorized,
                                             boolean isSurface) {
        if (isSurface)
            return sendCommand(String.format("SELECT userID " +
                "FROM UserBusiness U " +
                "WHERE NOT EXISTS ( " +
                "    (SELECT licenseID " +
                "    FROM GroundWaterLicense GL, DrawsGround D " +
                "    WHERE GL.licenseID = D.licenseID " +
                "AND D.waterID = %s " +
                "AND GL.dateAuthorized = %s) " +
                "    EXCEPT " +
                "    (SELECT licenseID " +
                "    FROM GroundWaterLicense GL " +
                "    WHERE U.userID = GL.userID) " +
                ")", waterID, dateAuthorized));
        else
            return sendCommand(String.format("SELECT userID " +
                    "FROM UserBusiness U " +
                    "WHERE NOT EXISTS ( " +
                    "    (SELECT licenseID " +
                    "    FROM SurfaceWaterLicense SL, DrawsSurface D " +
                    "    WHERE SL.licenseID = D.licenseID " +
                    "AND D.waterID = %s " +
                    "AND SL.dateAuthorized = %s) " +
                    "    EXCEPT " +
                    "    (SELECT licenseID " +
                    "    FROM SurfaceWaterLicense GL " +
                    "    WHERE U.userID = SL.userID) " +
                    ")", waterID, dateAuthorized));
    }

    /**
     * Returns sewage plants that handle all locations with at least one user with bad water.
     *
     * @param attributesGoHere Placeholder.
     * @return Aforementioned list.
     */
    public ArrayList<StringBuilder> problemPlant(String attributesGoHere) {
        return sendCommand("SELECT plantID " +
                "FROM SewagePlant SP " +
                "WHERE NOT EXISTS( " +
                "    (SELECT location " +
                "    FROM UserBusiness U, SewagePlantHandles S " +
                "    WHERE U.location = S.location " +
                "AND U.isWaterClean = False " +
                "GROUP BY location " +
                "HAVING COUNT(userID) >= 1) " +
                "    EXCEPT " +
                "    (SELECT location " +
                "    FROM SewagePlantHandles SPH " +
                "    WHERE SP.plantID = SPH.plantID) " +
                ")");
    }
}
