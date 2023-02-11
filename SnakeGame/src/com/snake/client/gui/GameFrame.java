package com.snake.client.gui;

import com.snake.client.gui.GamePanel;
import com.snake.client.networking.Client;

import javax.swing.*;

public class GameFrame extends JFrame{
    public GameFrame(Client client) {
        this.add(new GamePanel(client));
        System.out.println(client.clientUsername + " started the gameplay");
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
