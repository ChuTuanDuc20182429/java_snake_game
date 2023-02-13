package com.snake.client.gui;

import com.snake.client.networking.Client;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class GameFrame extends JFrame {
    public GameFrame(Client client) {
        // this.add(new GamePanel(client));
        this.add(new GamePanel(client));
        System.out.println(client.clientUsername + " started the gameplay");
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window closing");
                try {
                    client.getSocket().close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
