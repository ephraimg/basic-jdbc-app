package com.ephraimglick.basicjdbcapp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import org.postgresql.ds.PGSimpleDataSource;

public class BasicJdbcApp {
    public static void main(String[] args) throws IOException, SQLException {
        String DB_URL = "jdbc:postgresql://localhost/";
        String USER = "postgres";
        String PASS = "password";

        initDb(DB_URL, USER, PASS);

        // Configuration can be done like this if not configuring in persistence.xml
        Map<String, Object> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        props.put("jakarta.persistence.jdbc.url", DB_URL);
        props.put("jakarta.persistence.jdbc.user", USER);
        props.put("jakarta.persistence.jdbc.password", PASS);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("basic-jdbc-persistence-unit", props);

        // If configuring in persistence.xml, you can just pass in the persistence unit name
        // EntityManagerFactory emf = Persistence.createEntityManagerFactory("basic-jdbc-persistence-unit");

        EntityManager entityManager = emf.createEntityManager();
        saveDataFromUser(entityManager);
    }

    // Normally db would be created via migration - this is just for demo purposes
    private static void initDb(String dbUrl, String user, String pass) throws SQLException {
        // Using this datasource just for initDb (normally db would be created via migration)
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(dbUrl);
        Connection conn = dataSource.getConnection(user, pass);

        Statement stmt = conn.createStatement();
        String sql = "" +
                "CREATE SCHEMA IF NOT EXISTS music;" +
                "CREATE TABLE IF NOT EXISTS music.artists (artist_name VARCHAR(25) PRIMARY KEY, rating smallint);";
        stmt.executeUpdate(sql);
        System.out.println("Database set up successfully...");
    }

    private static void saveDataFromUser(EntityManager entityManager) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter an artist");
        String name = reader.readLine();
        System.out.println("Enter a rating (0 - 4)");
        int rating = Integer.parseInt(reader.readLine());

        Artist artist = new Artist(name, rating);
        entityManager.getTransaction().begin();
        entityManager.persist(artist);
        entityManager.getTransaction().commit();

        System.out.println("Saved artist " + name + " with rating " + rating);
    }
}
