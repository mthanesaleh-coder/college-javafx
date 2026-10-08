package com.college.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    // ═══════ إعدادات الاتصال ═══════
    private static final String SERVER = "127.0.0.1";
    private static final String DATABASE = "CollegeDB";
    private static final String USER = "sa";
    private static final String PASSWORD = "Admin123!";
    
    // ═══════ URL مع دعم Unicode (للعربي) ═══════
    private static final String URL = 
        "jdbc:sqlserver://" + SERVER + ";"
        + "databaseName=" + DATABASE + ";"
        + "encrypt=true;trustServerCertificate=true;"
        + "characterEncoding=UTF-8;"
        + "sendStringParametersAsUnicode=true";
    
    private static Connection connection = null;
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to SQL Server: " + DATABASE);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("SUCCESS: Connected to database!");
        } else {
            System.out.println("FAILED: Could not connect");
        }
        closeConnection();
    }
}