package com.example.libraryproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SqlController {
    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/OussamaDB", "root", "12345678");

            return connect;
        }catch(Exception e){e.printStackTrace();}

        return null;
    }

}

