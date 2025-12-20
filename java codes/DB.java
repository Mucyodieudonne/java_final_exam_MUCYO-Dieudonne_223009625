package com.IoT;

import java.sql.*;
import javax.swing.JOptionPane;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/iot_assistant_system";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");
            return conn;
        } catch (Exception e) {
            e.printStackTrace(); // full details in console
            JOptionPane.showMessageDialog(null, "❌ Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
