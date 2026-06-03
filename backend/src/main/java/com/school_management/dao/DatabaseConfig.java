package com.school_management.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties props = new Properties();
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASS;

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("⚠️ Warning: db.properties not found in resources. Using environment fallbacks.");
                DB_URL = "jdbc:mysql://localhost:3306/school_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&defaultAuthenticationPlugin=com.mysql.cj.jdbc.authentication.MysqlNativePasswordPlugin";
                DB_USER = "root";
                DB_PASS = "thinhlqd";
            } else {
                props.load(input);
                DB_URL = props.getProperty("db.url");
                DB_USER = props.getProperty("db.user");
                DB_PASS = props.getProperty("db.password");
            }
        } catch (IOException ex) {
            System.err.println("❌ Critical Error: Failed to load database configuration file.");
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found", e);
        }
    }
}