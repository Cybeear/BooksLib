package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionServicePostgresImpl implements ConnectionServiceInterface {
    private String DB_URL;
    private String DB_USER;
    private String DB_PASS;

    public ConnectionServicePostgresImpl() {
        DB_URL = "jdbc:postgresql://localhost:49153/bookslib";
        DB_USER = "postgres";
        DB_PASS = "postgrespw";
    }

    public ConnectionServicePostgresImpl(String dbUrl, String dbUser, String dbPass) {
        DB_URL = dbUrl;
        DB_USER = dbUser;
        DB_PASS = dbPass;
    }

    public String getDbUrl() {
        return DB_URL;
    }

    public void setDbUrl(String dbUrl) {
        DB_URL = dbUrl;
    }

    public String getDbUser() {
        return DB_USER;
    }

    public void setDbUser(String dbUser) {
        DB_USER = dbUser;
    }

    public String getDbPass() {
        return DB_PASS;
    }

    public void setDbPass(String dbPass) {
        DB_PASS = dbPass;
    }

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