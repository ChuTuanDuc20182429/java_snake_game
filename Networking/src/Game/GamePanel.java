package Game;

import Networking.Client;
import Networking.Packet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private Client client;
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 15;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 500;
    public boolean player1 = false;
    int appleX, appleY;
    private Snake snake1 = new Snake(GAME_UNITS, 3, 'R');
    private Snake snake2 = new Snake(GAME_UNITS, 3, 'R');

    boolean running = false;
    Timer timer;
    Random random;
    KeyBinding keyBinding;

    GamePanel(Client client) {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.client = client;
        this.setFocusable(true);
        keyBinding = new KeyBinding(this, client.clientUsername, snake1, client);
        while (!client.setup) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        startGame();
    }

    private void startGame() {
        System.out.println("start");
        if (client.setup_Packet.snake) {
            player1 = true;
            snake1.x[0] = client.setup_Packet.init_posx_snake1 * UNIT_SIZE;
            snake1.y[0] = client.setup_Packet.init_posy_snake1 * UNIT_SIZE;
            snake2.x[0] = client.setup_Packet.init_posx_snake2 * UNIT_SIZE;
            snake2.y[0] = client.setup_Packet.init_posy_snake2 * UNIT_SIZE;
            snake1.direction = client.setup_Packet.direction_snake1;
            snake2.direction = client.setup_Packet.direction_snake2;
            System.out.println("paket setup prepared");

            Packet p = new Packet(client.clientUsername, snake1.x[0], snake1.y[0], snake2.x[0], snake2.y[0],
                    snake1.direction, snake2.direction, true, true);
            client.sendPacket(p);
            System.out.println("paket setup sended");
        } else {
            player1 = false;
            snake1.x[0] = client.setup_Packet.init_posx_snake1 * UNIT_SIZE;
            snake1.y[0] = client.setup_Packet.init_posy_snake1 * UNIT_SIZE;
            snake2.x[0] = client.setup_Packet.init_posx_snake2 * UNIT_SIZE;
            snake2.y[0] = client.setup_Packet.init_posy_snake2 * UNIT_SIZE;
            snake1.direction = client.setup_Packet.direction_snake1;
            snake2.direction = client.setup_Packet.direction_snake2;
            System.out.println("paket setup prepared");

            Packet p = new Packet(client.clientUsername, snake1.x[0], snake1.y[0], snake2.x[0], snake2.y[0],
                    snake1.direction, snake2.direction, true, false);
            client.sendPacket(p);
            System.out.println("paket setup sended");
        }

        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // Apple coordinates
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void drawSnake(Graphics g, Snake snake, Color headColor, Color bodyColor, int UNIT_SIZE) {
        for (int i = 0; i < snake.bodyParts; i++) {
            if (i == 0) {
                g.setColor(headColor);
                g.fillRect(snake.x[i], snake.y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(bodyColor);
                g.fillRect(snake.x[i], snake.y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    public void draw(Graphics g) {
        if (running) {
            // for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            // g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // draw vertical
            // lines
            // g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // draw horizontal
            // lines
            // }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            drawSnake(g, snake1, Color.green, new Color(45, 100, 0), UNIT_SIZE);
            drawSnake(g, snake2, Color.yellow, new Color(190, 204, 0), UNIT_SIZE);
            g.setColor(Color.blue);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + snake1.applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("Score: " + snake1.applesEaten)) / 2, 40);
        } else {
            gameOver(g);
            // startGame();
        }
    }

    public void moveSnake(Snake snake) {
        for (int i = snake.bodyParts; i > 0; i--) {
            snake.x[i] = snake.x[i - 1]; // x[0], y[0] is the coordinate of snake's head
            snake.y[i] = snake.y[i - 1];
        }

        switch (snake.direction) {
            case 'U':
                snake.y[0] = snake.y[0] - UNIT_SIZE; // since the origin is located at top left
                break;
            case 'D':
                snake.y[0] = snake.y[0] + UNIT_SIZE;
                break;
            case 'L':
                snake.x[0] = snake.x[0] - UNIT_SIZE;
                break;
            case 'R':
                snake.x[0] = snake.x[0] + UNIT_SIZE;
                break;
        }
    }

    public void moveSnake(Snake snake, Client client) {
        for (int i = snake.bodyParts; i > 0; i--) {
            snake.x[i] = snake.x[i - 1]; // x[0], y[0] is the coordinate of snake's head
            snake.y[i] = snake.y[i - 1];
        }

        switch (client.snake_remote_direction) {
            case 'U':
                System.out.println("snake opponent U");
                snake.y[0] = snake.y[0] - UNIT_SIZE; // since the origin is located at top left
                break;
            case 'D':
                System.out.println("snake opponent D");
                snake.y[0] = snake.y[0] + UNIT_SIZE;
                break;
            case 'L':
                System.out.println("snake opponent L");
                snake.x[0] = snake.x[0] - UNIT_SIZE;
                break;
            case 'R':
                System.out.println("snake opponent R");
                snake.x[0] = snake.x[0] + UNIT_SIZE;
                break;
        }
    }

    public void move() {
        // loop to iterate all body parts of the snake
        if (player1) {
            System.out.println("player1");
            moveSnake(snake1);
            moveSnake(snake2, client);
        } else {
            System.out.println("player2");
            moveSnake(snake2);
            moveSnake(snake1, client);
        }

    }

    public void checkApple() {
        if ((snake1.x[0] == appleX) && snake1.y[0] == appleY) {
            snake1.bodyParts++;
            snake1.applesEaten++;
            newApple();
        }
        if ((snake2.x[0] == appleX) && snake2.y[0] == appleY) {
            snake2.bodyParts++;
            snake2.applesEaten++;
            newApple();
        }
    }

    public void checkSnakeCollision(Snake snake) {
        for (int i = snake.bodyParts; i > 0; i--) {
            if ((snake.x[0] == snake.x[i]) && (snake.y[0] == snake.y[i])) {
                running = false;
            }
        }
        // check if head touches left border
        if (snake.x[0] < 0) {
            running = false;
        }
        // check if head touches right border
        if (snake.x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // check if head touches top border
        if (snake.y[0] < 0) {
            running = false;
        }
        // check if head touches bottom border
        if (snake.y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    public void checkCollision() {
        // check if head collides with body
        // checkSnakeCollision(snake1);
        // checkSnakeCollision(snake2);
    }

    public void gameOver(Graphics g) {
        // Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))
                / 2, SCREEN_HEIGHT / 2);

        g.setColor(Color.blue);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Your score: " + snake1.applesEaten,
                (SCREEN_WIDTH - metrics2.stringWidth("Your score: " + snake1.applesEaten)) / 2, 40);

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 15));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press Enter to play again",
                (SCREEN_WIDTH - metrics3.stringWidth("Press Enter to play again")) / 2, 500);
        // while (keyBinding.press_enter) {
        // startGame();
        // }

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
}
