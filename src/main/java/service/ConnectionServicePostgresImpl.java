package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionServicePostgresImpl implements ConnectionServiceInterface {
    protected static final String DB_URL = "jdbc:postgresql://localhost:49153/bookslib";
    protected static final String DB_USER = "postgres";
    protected static final String DB_PASS = "postgrespw";

    @Override
    public Connection createConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException sqlException) {
            System.err.println("Error with JDBC create connection" + sqlException.getMessage());
            System.exit(1);
        }
        return null;
    }
}