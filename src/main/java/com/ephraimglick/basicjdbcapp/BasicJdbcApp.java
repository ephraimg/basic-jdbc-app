package com.ephraimglick.basicjdbcapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class BasicJdbcApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        String DB_URL = "jdbc:postgresql://localhost/";
        String USER = "postgres";
        String PASS = "password";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            initDb(conn);
            saveDataFromUser(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initDb(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "" +
                "CREATE SCHEMA IF NOT EXISTS music;" +
                "CREATE TABLE IF NOT EXISTS music.artists (artist_name VARCHAR(25) PRIMARY KEY, rating smallint);";
        stmt.executeUpdate(sql);
        System.out.println("Database set up successfully...");
    }

    private static void saveDataFromUser(Connection conn) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter an artist");
        String name = reader.readLine();
        System.out.println("Enter a rating (0 - 4)");
        int rating = Integer.parseInt(reader.readLine());

        String query = "INSERT INTO music.artists (artist_name, rating) values (?, ?);";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setInt(2, rating);
        stmt.execute();
        System.out.println("Saved artist " + name + " with rating " + rating);
    }
}
