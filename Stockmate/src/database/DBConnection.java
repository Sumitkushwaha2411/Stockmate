package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {

        String url = "jdbc:mysql://localhost:3306/stockmate_db";
        String user = "root";
        String password = "apache24"; // change this

        return DriverManager.getConnection(url, user, password);
    }
}