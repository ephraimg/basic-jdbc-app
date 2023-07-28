package com.ephraimglick.basicjdbcapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.postgresql.ds.PGSimpleDataSource;

public class BasicJdbcApp {
    public static void main(String[] args) throws IOException, SQLException {
        String DB_URL = "jdbc:postgresql://localhost/";
        String USER = "postgres";
        String PASS = "password";

        initDb(DB_URL, USER, PASS);

        // Configuration can be done like this if not configuring in hibernate.cfg.xml
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.url", DB_URL);
        properties.setProperty("hibernate.connection.username", USER);
        properties.setProperty("hibernate.connection.password", PASS);
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "create"); // Drops tables then auto-creates
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(properties);
        Configuration hibernateConfiguration = new Configuration().setProperties(properties);
        SessionFactory sessionFactory = hibernateConfiguration
                .addAnnotatedClass(Artist.class)
                .buildSessionFactory(builder.build());

        saveDataFromUser(sessionFactory);

    }

    // Normally db would be created via migration - this is just for demo purposes.
    // Hibernate can create tables based on your entities
    // It could also create the schema using org.hibernate.tool.hbm2ddl.SchemaExport
    private static void initDb(String dbUrl, String user, String pass) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(dbUrl);
        Connection conn = dataSource.getConnection(user, pass);

        Statement stmt = conn.createStatement();
        String sql = "CREATE SCHEMA IF NOT EXISTS music;";
          stmt.executeUpdate(sql);
        System.out.println("Database set up successfully...");
    }

    private static void saveDataFromUser(SessionFactory sessionFactory) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter an artist");
        String name = reader.readLine();
        System.out.println("Enter a rating (0 - 4)");
        int rating = Integer.parseInt(reader.readLine());

        Artist artist = new Artist(name, rating);
        sessionFactory.inTransaction(transaction -> {
            transaction.persist(artist);
        });

        System.out.println("Saved artist " + name + " with rating " + rating);
    }
}
