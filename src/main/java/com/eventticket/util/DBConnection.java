//package com.eventticket.util;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DBConnection {
//    private static final String URL =
//            "jdbc:mysql://localhost:3306/event_ticket_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
//    private static final String USER = "root";
//    private static final String PASSWORD = "root";
//
//    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            throw new ExceptionInInitializerError("MySQL JDBC Driver not found. Add mysql-connector jar to libraries.");
//        }
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }
//
//    private DBConnection() {
//    }
//}



package com.eventticket.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

   private static final String URL =
"jdbc:mysql://localhost:3306/eventticketdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

private static final String USER = "root";
private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "MySQL JDBC Driver not found. Add mysql-connector jar to libraries."
            );
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private DBConnection() {
    }
}