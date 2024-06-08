package com.hecto.fitnessuniv;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        try {
            String url =
                    "jdbc:mysql://217.178.40.77:3306/fitnessuniv?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
            String username = "dev";
            String password = "3300";

            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
