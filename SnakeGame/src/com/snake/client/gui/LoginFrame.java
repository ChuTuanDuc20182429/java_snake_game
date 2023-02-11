package com.snake.client.gui;

import com.snake.client.networking.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class LoginFrame {
    public static void main(String[] args) {
        JFrame f = new JFrame("Enter Username");
        final JTextField tf = new JTextField();
        tf.setBounds(50,50, 150,20);
        JButton b = new JButton("PLay");
        b.setBounds(50,100,95,30);
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)  {
                try {
                    f.dispose();
                    Socket socket = new Socket("localhost", 1234);
                    Client client = new Client(socket, tf.getText());

                    client.listenForPacket();
                    new GameFrame(client);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        f.add(b);f.add(tf);
        f.setSize(400,400);
        f.setLayout(null);
        f.setVisible(true);
    }
}
