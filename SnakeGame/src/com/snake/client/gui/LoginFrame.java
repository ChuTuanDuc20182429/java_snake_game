package com.snake.client.gui;

import com.snake.client.networking.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;

public class LoginFrame {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/HighScore";
        String username = "debian-sys-maint";
        String password = "CNCTEDLTOWKK1fFS";
        JFrame f = new JFrame("Enter Username");
        final JTextField tf = new JTextField();
        tf.setBounds(50, 50, 150, 20);
        JButton b = new JButton("PLay");
        b.setBounds(50, 100, 95, 30);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    f.dispose();
                    Socket socket = new Socket("192.168.0.116", 1234);
                    Client client = new Client(socket, tf.getText());

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, username, password);
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("insert into HighScore values ('" + tf.getText() + "', 0)");

                    connection.close();
                    client.sendInitRequest();
                    client.listenForPacket();
                    new GameFrame(client);

                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        f.add(b);
        f.add(tf);
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
    }
}
