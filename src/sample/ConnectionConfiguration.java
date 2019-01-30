package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionConfiguration {

    public static Connection getConnection() {
        Connection connection = null;
        String url = "jdbc:postgresql://dumbo.db.elephantsql.com:5432/oxlkferp";
        String username = "USERNAME";
        String password = "PASSWORD";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
