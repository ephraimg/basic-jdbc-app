package com.ephraimglick.basicjdbcapp;

import org.jdbi.v3.core.Jdbi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class BasicJdbcApp {
    public static void main(String[] args) throws IOException, SQLException {
        String DB_URL = "jdbc:postgresql://localhost/";
        String USER = "postgres";
        String PASS = "password";

        Jdbi jdbi = Jdbi.create(DB_URL, USER, PASS);
        initDb(jdbi);
        saveDataFromUser(jdbi);
    }

    private static void initDb(Jdbi jdbi) {
        String sql = "" +
                "CREATE SCHEMA IF NOT EXISTS music;" +
                "CREATE TABLE IF NOT EXISTS music.artists (artist_name VARCHAR(25) PRIMARY KEY, rating smallint);";
        jdbi.useHandle(handle -> handle.execute(sql));
        System.out.println("Database set up successfully...");
    }

    private static void saveDataFromUser(Jdbi jdbi) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter an artist");
        String name = reader.readLine();
        System.out.println("Enter a rating (0 - 4)");
        int rating = Integer.parseInt(reader.readLine());

        String query = "INSERT INTO music.artists (artist_name, rating) values (?, ?);";
        jdbi.useHandle(handle -> handle.execute(query, name, rating));
        System.out.println("Saved artist " + name + " with rating " + rating);
    }
}
