package com.snake.client.gui;

import com.snake.client.networking.Client;
import com.snake.packets.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class KeyBinding {
    public Action leftAction;
    public Action rightAction;
    public Action upAction;
    public Action downAction;
    public Action playAgain;
    String username;
    Snake snake;
    Client client;

    public KeyBinding(JPanel jPanel, String username, Snake snake, Client client) {
        leftAction = new KeyBinding.LeftAction();
        rightAction = new KeyBinding.RightAction();
        upAction = new KeyBinding.UpAction();
        downAction = new KeyBinding.DownAction();

        this.username = username;
        this.snake = snake;

        jPanel.getInputMap().put(KeyStroke.getKeyStroke("A"), "leftAction");
        jPanel.getActionMap().put("leftAction", leftAction);
        jPanel.getInputMap().put(KeyStroke.getKeyStroke("D"), "rightAction");
        jPanel.getActionMap().put("rightAction", rightAction);
        jPanel.getInputMap().put(KeyStroke.getKeyStroke("W"), "upAction");
        jPanel.getActionMap().put("upAction", upAction);
        jPanel.getInputMap().put(KeyStroke.getKeyStroke("S"), "downAction");
        jPanel.getActionMap().put("downAction", downAction);
        jPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "PlayAgain");
        jPanel.getActionMap().put("PlayAgain", playAgain);

        this.client = client;
    }

    private class LeftAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            // if (snake.direction != 'R') {
            snake.direction = 'L';
            // }
            System.out.println("L");
            PlayerDataPacket packet = new PlayerDataPacket(username, snake.direction);
            client.sendPacket(packet);
        }
    }

    private class RightAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            // if (snake.direction != 'L') {
            snake.direction = 'R';
            // }
            System.out.println("R");

            PlayerDataPacket packet = new PlayerDataPacket(username, snake.direction);
            client.sendPacket(packet);
        }
    }

    private class UpAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            // if (snake.direction != 'D') {
            snake.direction = 'U';
            // }
            System.out.println("U");

            PlayerDataPacket packet = new PlayerDataPacket(username, snake.direction);
            client.sendPacket(packet);
        }
    }

    private class DownAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            // if (snake.direction != 'U') {
            snake.direction = 'D';
            // }
            System.out.println("D");

            PlayerDataPacket packet = new PlayerDataPacket(username, snake.direction);
            client.sendPacket(packet);
        }
    }

}
