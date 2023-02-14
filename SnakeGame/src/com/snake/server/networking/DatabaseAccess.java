package com.snake.server.networking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAccess {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseAccess(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void addNewRecord(String playerName) {
         try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into HighScore values ('" + playerName + "', 0)");

            connection.close();
        } catch (SQLException e) {
             throw new RuntimeException(e);
         } catch (ClassNotFoundException e) {
             throw new RuntimeException(e);
         }
    }

    public void updatePlayerScore(int score, String playerName) {
        try {
            String url = "jdbc:mysql://localhost:3306/HighScore";
            String username = "debian-sys-maint";
            String password = "CNCTEDLTOWKK1fFS";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("update HighScore set Score = " + score + " where Username = '" + playerName + "'");

            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
