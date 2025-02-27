package Application.MTCG.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {

    private final static String URL = "jdbc:postgresql://localhost:5433/mtcg";
    private final static String USERNAME = "marvin1";
    private final static String PASSWORD = "marvin1";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
