package dal;

import com.sun.rowset.CachedRowSetImpl;
import logic.ConfigLoader;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Forbindelse til database
 */
public class MYSQLDriver {

    private static Connection dbConnection = null;
    private static final String URL = ConfigLoader.DB_TYPE+ConfigLoader.DB_HOST+ConfigLoader.DB_PORT+"/"+ConfigLoader.DB_NAME+"?autoReconnect=true&useSSL=false";
    private static final String USERNAME = ConfigLoader.DB_USER;
    private static final String PASSWORD = ConfigLoader.DB_PASS;

    public MYSQLDriver(){

    }

    /**
     * Eksekver et SQL statement på databasen og returner det påvirkede <code>ResultSet</code>.
     * @param sql det SQL statement der ønskes eksekveret.
     * @return det påvirkede <code>ResultSet</code>.
     * @throws SQLException
     */
    public static CachedRowSet executeSQL(String sql) throws SQLException {

        ResultSet result = null;
        CachedRowSet cr = new CachedRowSetImpl();
        try {
            dbConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            result = dbConnection.prepareStatement(sql).executeQuery();
            cr.populate(result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.close();
        }

        return cr;
    }

    /**
     * Eksekver et SQL statement på databasen.
     * @param sql det SQL statement der ønskes eksekveret.
     * @throws SQLException
     */
    public static void updateSQL(String sql) throws SQLException{

        try{
            dbConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            dbConnection.prepareStatement(sql).executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            dbConnection.close();
        }
    }

}
