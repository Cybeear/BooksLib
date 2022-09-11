package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionService {
    protected static final String DB_URL = "jdbc:postgresql://localhost:49153/Bookslib";
    protected static final String DB_USER = "postgres";
    protected static final String DB_PASS = "postgrespw";

    public Connection createConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}