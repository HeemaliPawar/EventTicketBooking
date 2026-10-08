package com.eventticket;

import com.eventticket.util.DBConnection;
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try {
            Connection con = DBConnection.getConnection();

            if (con != null) {
                System.out.println("SUCCESS: MySQL Connected!");
                con.close();
            }

        } catch (Exception e) {
            System.out.println("DATABASE CONNECTION FAILED!");
            e.printStackTrace();
        }
    }
}