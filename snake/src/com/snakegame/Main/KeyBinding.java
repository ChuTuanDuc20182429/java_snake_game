package com.snakegame.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class KeyBinding {
    public Action leftAction;
    public Action rightAction;
    public Action upAction;
    public Action downAction;
    public String keyCode;
    String username;
    Snake snake;
//
    public KeyBinding(JPanel jPanel, String username, Snake snake) {
//
        leftAction = new LeftAction();
        rightAction = new RightAction();
        upAction = new UpAction();
        downAction = new DownAction();
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


    }
    private class LeftAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println(actionEvent.getActionCommand());
            if (snake.direction != 'R') {
                snake.direction = 'L';

            }
//            label.setLocation(label.getX() - 5, label.getY());

        }
    }

    private class RightAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            System.out.println(actionEvent.getActionCommand());
            if (snake.direction != 'L') {
                snake.direction = 'R';

            }

        }
    }

    private class UpAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            System.out.println(actionEvent.getActionCommand());
            if (snake.direction != 'D') {
                snake.direction = 'U';

            }
//            label.setLocation(label.getX(), label.getY() - 5);

        }
    }

    private class DownAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            System.out.println(actionEvent.getActionCommand());
            if (snake.direction != 'U') {
                snake.direction = 'D';

            }
//            label.setLocation(label.getX(), label.getY() + 5);

        }
    }
    public String getKeyCode() {
        return keyCode;
    }

//    public static void main(String[] args) {
//        JFrame myFrame;
//        myFrame = new JFrame();
//        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        myFrame.setSize(800, 600);
//        myFrame.setLayout(null);
//        JRootPane rootPane = myFrame.getRootPane();
//        myFrame.setVisible(true);
//
//
//        KeyBinding keyBinding = new KeyBinding(rootPane);
//        while(true) {
//            System.out.println(keyBinding.getKeyCode());
//        }
//    }
}
