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
        JButton bPlay = new JButton("PLay");
        bPlay.setBounds(50, 100, 95, 30);
        bPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    f.dispose();
                    Socket socket = new Socket("localhost", 1234);
                    Client client = new Client(socket, tf.getText());

                    // connection.close();
                    client.sendInitRequest();
                    client.listenForPacket();
                    new GameFrame(client);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JButton bScore = new JButton("Score");
        bScore.setBounds(50, 150, 95, 30);
        bScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, username, password);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from HighScore order by Score desc limit 4");
                    String[][] data = new String[4][2];
                    int row = 0;
                    while (resultSet.next()) {
                        int index = 1;
                        for (int j = 0; j < 2; j++) {
                            data[row][j] = resultSet.getString(index);
                            index++;

                        }
                        row++;
                    }
                    JFrame frame = new JFrame();
                    frame.setTitle("Highscore");
                    String[] columnNames = {"Name", "Score"};
                    JTable j = new JTable(data, columnNames);
                    j.setBounds(30, 40, 200, 300);
                    JScrollPane sp = new JScrollPane(j);
                    frame.add(sp);
                    // Frame Size
                    frame.setSize(500, 200);
                    // Frame Visible = true
                    frame.setVisible(true);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        f.add(bScore);
        f.add(bPlay);
        f.add(tf);
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
    }
}
