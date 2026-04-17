package com.learninglog.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

//Making connection Between Java and Database
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/localshop";
    ;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // we have not set password in Database

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found: " + e.getMessage());
        }
    }

    // making method for connecting database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    //  Now closing the Database connection and making static it just print message in error it doesnt return any value back
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.printf("Error while Closting the Database Connection : " + ex.getMessage());
        }
    }
}
