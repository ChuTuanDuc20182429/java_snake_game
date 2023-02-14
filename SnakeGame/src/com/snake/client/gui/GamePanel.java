package com.snake.client.gui;

import com.snake.client.networking.Client;
import com.snake.packets.GameInitPacket;
import com.snake.packets.GameStatePacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GamePanel extends JPanel implements ActionListener {
    Client client;
    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;
    int UNIT_SIZE;
    int GAME_UNITS;
    int DELAY;
    int appleX, appleY;
    private Snake snake1 = new Snake();
    private Snake snake2 = new Snake();
    private boolean Isplayer1Win = false;
    private boolean Isplayer2Win = false;

    public static boolean running = false;
    private Timer timer;

    private KeyBinding keyBinding;

    GamePanel(Client client) {
        while (true) {
            if (initGame(client.getGameInitPacket())) {
                break;
            }
        }
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.client = client;
        if (client.clientUsername.equals(snake1.playerName)) {
            keyBinding = new KeyBinding(this, client.clientUsername, snake1, client);
        } else {
            keyBinding = new KeyBinding(this, client.clientUsername, snake2, client);
        }
        startGame();
        System.out.println("Game started");
    }

    private void startGame() {

        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void drawSnake(Graphics g, final Snake snake, Color headColor, Color bodyColor, int UNIT_SIZE) {
        Snake n = new Snake(snake);
        g.setColor(headColor);
        g.fillRect(n.getHead()[0], n.getHead()[1], UNIT_SIZE, UNIT_SIZE);
        n.popHead();

        g.setColor(bodyColor);
        for (int[] p : n.getBody()) {
            g.fillRect(p[0], p[1], UNIT_SIZE, UNIT_SIZE);
        }
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.CYAN);
            g.setFont(new Font("Ink Free", Font.BOLD, 10));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Player: " + client.clientUsername,
                    (SCREEN_WIDTH - metrics1.stringWidth("Player: " + client.clientUsername)) / 2, 560);

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            drawSnake(g, snake1, Color.green, new Color(45, 100, 0), UNIT_SIZE);
            drawSnake(g, snake2, Color.yellow, new Color(190, 204, 0), UNIT_SIZE);
            g.setColor(Color.blue);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score player 1: " + snake1.applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("Score player 1: " + snake1.applesEaten)) / 2, 20);
            g.drawString("Score player 2: " + snake2.applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("Score player 2: " + snake1.applesEaten)) / 2, 40);
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        if ((client.clientUsername.equals(snake1.playerName) && Isplayer1Win)
                || (client.clientUsername.equals(snake2.playerName) && Isplayer2Win)) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString("YOU WIN", (SCREEN_WIDTH - metrics3.stringWidth("YOU WIN"))
                    / 2, SCREEN_HEIGHT / 2);
            // scoreUpdated(snake1.applesEaten, snake1.playerName);
            // scoreUpdated(snake2.applesEaten, snake2.playerName);
        } else {
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString("YOU LOSE", (SCREEN_WIDTH - metrics3.stringWidth("YOU LOSE"))
                    / 2, SCREEN_HEIGHT / 2);
            // scoreUpdated(snake1.applesEaten, snake1.playerName);
            // scoreUpdated(snake2.applesEaten, snake2.playerName);
        }

        g.setColor(Color.blue);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        if (snake1.playerName.equals(client.clientUsername)) {
            g.drawString("Your score: " + snake1.applesEaten,
                    (SCREEN_WIDTH - metrics2.stringWidth("Your score: " + snake1.applesEaten)) / 2, 40);
        } else {
            g.drawString("Your score: " + snake2.applesEaten,
                    (SCREEN_WIDTH - metrics2.stringWidth("Your score: " + snake2.applesEaten)) / 2, 40);
        }

    }

    private void updateGame(GameStatePacket packet) {
        if (packet == null) {
            return;
        }
        // Snake 1
        this.snake1.applesEaten = packet.snake1_data.applesEaten;
        this.snake1.bodyParts = packet.snake1_data.bodyParts;
        this.snake1.direction = packet.snake1_data.direction;
        this.snake1.setBody(packet.snake1_data.body);
        this.snake1.playerName = packet.snake1_data.playerName;
        // System.out.println("playerName 1: " + this.snake1.playerName);

        // Snake 2
        this.snake2.applesEaten = packet.snake2_data.applesEaten;
        this.snake2.bodyParts = packet.snake2_data.bodyParts;
        this.snake2.direction = packet.snake2_data.direction;
        this.snake2.setBody(packet.snake2_data.body);
        this.snake2.playerName = packet.snake2_data.playerName;
        // System.out.println("playerName 2: " + this.snake2.playerName);

        // Apple
        this.appleX = packet.appleX;
        this.appleY = packet.appleY;
        // System.out.println("apple :" + this.appleX + " " + this.appleY);

        if (packet.getCollisionStatus_snake1()) {
            this.Isplayer1Win = packet.Isplayer1Win;
            this.Isplayer2Win = packet.Isplayer2Win;
            System.out.println(this.Isplayer1Win + " " + this.Isplayer2Win);
            running = false;
        } else if (packet.getCollisionStatus_snake2()) {
            this.Isplayer1Win = packet.Isplayer1Win;
            this.Isplayer2Win = packet.Isplayer2Win;
            System.out.println(this.Isplayer1Win + " " + this.Isplayer2Win);

            running = false;
        }

    }

    private boolean initGame(GameInitPacket gameInitPacket) {
        if (gameInitPacket == null) {
            return false;
        }
        this.SCREEN_WIDTH = gameInitPacket.SCREEN_WIDTH;
        this.SCREEN_HEIGHT = gameInitPacket.SCREEN_HEIGHT;
        this.DELAY = gameInitPacket.DELAY;
        this.GAME_UNITS = gameInitPacket.GAME_UNITS;
        this.UNIT_SIZE = gameInitPacket.UNIT_SIZE;
        // Snake 1
        this.snake1.applesEaten = gameInitPacket.snake1_data.applesEaten;
        this.snake1.bodyParts = gameInitPacket.snake1_data.bodyParts;
        this.snake1.direction = gameInitPacket.snake1_data.direction;
        this.snake1.setBody(gameInitPacket.snake1_data.body);
        this.snake1.playerName = gameInitPacket.snake1_data.playerName;
        // System.out.println("this.snake1.playerName: " + this.snake1.playerName);

        // Snake 2
        this.snake2.applesEaten = gameInitPacket.snake2_data.applesEaten;
        this.snake2.bodyParts = gameInitPacket.snake2_data.bodyParts;
        this.snake2.direction = gameInitPacket.snake2_data.direction;
        this.snake2.setBody(gameInitPacket.snake2_data.body);
        this.snake2.playerName = gameInitPacket.snake2_data.playerName;
        // System.out.println("this.snake2.playerName: " + this.snake2.playerName);

        // Apple
        this.appleX = gameInitPacket.appleX;
        this.appleY = gameInitPacket.appleY;
        // System.out.println("apple :" + this.appleX + " " + this.appleY);
        return true;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (running) {
            updateGame(client.popQueue());
        }
        repaint();

    }

    public void scoreUpdated(int score, String playeName) {
        try {
            String url = "jdbc:mysql://192.168.0.116:3306/HighScore";
            String username = "tuan";
            String password = "password";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("update HighScore set Score = " + score + " where Username = '" + playeName + "'");

            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
