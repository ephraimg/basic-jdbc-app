package com.ephraimglick.basicjdbcapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BasicJdbcApp {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter an artist");
        String name = reader.readLine();
        System.out.println("Enter a rating (0 - 4)");
        String rating = reader.readLine();
        System.out.println(name + ": " + rating + " stars");
    }
}
