package com.snake.server.networking;

import com.snake.client.gui.Snake;
import com.snake.client.networking.SnakeGameplayData;
import com.snake.packets.GameInitPacket;
import com.snake.packets.GameStatePacket;
import com.snake.packets.PlayerDataPacket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class GameLogic {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 15;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 0;
    int appleX, appleY;
    private Random random;
    // private GameStatePacket gameStatePacket;
    SnakeGameplayData data_snake1;
    SnakeGameplayData data_snake2;
    Snake snake1;
    Snake snake2;
    private String playerName1 = "";
    private String playerName2 = "";
    private boolean snake1Collision = false;
    private boolean snake2Collision = false;
    private boolean Isplayer1Win = false;
    private boolean Isplayer2Win = false;

    private GameInitPacket gameInitPacket;
    private String url = "jdbc:mysql://localhost:3306/HighScore";
    private String username = "debian-sys-maint";
    private String password = "CNCTEDLTOWKK1fFS";
    private DatabaseAccess databaseAccess;

    public GameLogic() {
        random = new Random();
        setup_snake(playerName1, playerName2);
        newApple();
        gameInitPacket = new GameInitPacket(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, GAME_UNITS, DELAY, playerName1,
                playerName2, data_snake1, data_snake2, appleX, appleY);
        databaseAccess = new DatabaseAccess(this.url, this.username, this.password);

    }

    private void newApple() {
        this.appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        this.appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    private void setup_snake(String playerName1, String playerName2) {
        // snake player 1
        snake1 = new Snake(playerName1, GAME_UNITS, 3, 'R');
        snake1.setHead(0, 0);
        snake1.initBody(0, 0);
        data_snake1 = new SnakeGameplayData(snake1);

        // snake player 2
        snake2 = new Snake(playerName2, GAME_UNITS, 3, 'L');
        snake2.setHead(39 * UNIT_SIZE, 39 * UNIT_SIZE);
        snake2.initBody(39 * UNIT_SIZE, 39 * UNIT_SIZE);

        data_snake2 = new SnakeGameplayData(snake2);
    }

    public GameInitPacket getGameInitPacket() {
        return gameInitPacket;
    }

    public void setPlayers_name(String playName1, String playName2) {
        this.playerName1 = playName1;
        this.playerName2 = playName2;
        this.gameInitPacket.snake1_data.playerName = playName1;
        this.gameInitPacket.snake2_data.playerName = playName2;
        this.snake1.playerName = playName1;
        this.snake2.playerName = playName2;
    }

    public GameStatePacket calculateGameState(PlayerDataPacket p1, PlayerDataPacket p2) {
        if (p1 == null) {
            if (p2 == null) {
                return null;
            } else {
                snake2.direction = p2.direction;
                // moveSnake(snake1);
                moveSnake(snake2);
                data_snake1 = new SnakeGameplayData(snake1);
                data_snake2 = new SnakeGameplayData(snake2);
                checkCollision(data_snake1.body, data_snake2.body);
                checkApple(appleX, appleY, data_snake1.body.getFirst(), data_snake2.body.getFirst());
                return new GameStatePacket(data_snake1, data_snake2, appleX, appleY, snake1Collision, snake2Collision,
                        Isplayer1Win, Isplayer2Win);
            }
        } else {
            if (p2 == null) {
                snake1.direction = p1.direction;
                moveSnake(snake1);
                // moveSnake(snake2);
                data_snake1 = new SnakeGameplayData(snake1);
                data_snake2 = new SnakeGameplayData(snake2);
                checkCollision(data_snake1.body, data_snake2.body);

                checkApple(appleX, appleY, data_snake1.body.getFirst(), data_snake2.body.getFirst());

                return new GameStatePacket(data_snake1, data_snake2, appleX, appleY, snake1Collision, snake2Collision,
                        Isplayer1Win, Isplayer2Win);

            }
        }

        if (snake1.playerName.equals(p1.username)) {
            snake1.direction = p1.direction;
            moveSnake(snake1);

            snake2.direction = p2.direction;
            moveSnake(snake2);

        } else {
            System.out.println("I suppose this case will not happen");
            snake1.direction = p2.direction;
            moveSnake(snake1);

            snake2.direction = p2.direction;
            moveSnake(snake2);
        }
        data_snake1 = new SnakeGameplayData(snake1);
        data_snake2 = new SnakeGameplayData(snake2);
        checkCollision(data_snake1.body, data_snake2.body);
        checkApple(appleX, appleY, data_snake1.body.getFirst(), data_snake2.body.getFirst());
        return new GameStatePacket(data_snake1, data_snake2, appleX, appleY, snake1Collision, snake2Collision,
                Isplayer1Win, Isplayer2Win);
    }

    public void moveSnake(Snake snake) {
        snake.pop();
        int x = snake.getHead()[0];
        int y = snake.getHead()[1];
        switch (snake.direction) {
            case 'U':
                if (y == 0) {
                    snake.pushHead(x, SCREEN_HEIGHT - UNIT_SIZE);
                    break;
                }
                snake.pushHead(x, y - UNIT_SIZE);
                break;
            case 'D':
                if (y == (SCREEN_HEIGHT - UNIT_SIZE)) {
                    snake.pushHead(x, 0);
                    break;
                }
                snake.pushHead(x, y + UNIT_SIZE);
                break;
            case 'L':
                if (x == 0) {
                    snake.pushHead(SCREEN_WIDTH - UNIT_SIZE, y);
                    break;
                }
                snake.pushHead(x - UNIT_SIZE, y);
                break;
            case 'R':
                if (x == (SCREEN_WIDTH - UNIT_SIZE)) {
                    snake.pushHead(0, y);
                    break;
                }
                snake.pushHead(x + UNIT_SIZE, y);
                break;
        }
    }

    private void checkApple(int appleX, int appleY, int[] head_snake1, int[] head_snake2) {
        if (head_snake1[0] == appleX && head_snake1[1] == appleY) {
            snake1.applesEaten++;
            snake1.bodyParts++;
            snake1.pushTail(head_snake1[0], head_snake1[1]);
            newApple();
        } else if (head_snake2[0] == appleX && head_snake2[1] == appleY) {
            snake2.applesEaten++;
            snake2.bodyParts++;
            snake2.pushTail(head_snake2[0], head_snake2[1]);
            newApple();
        }
    }

    private void checkCollision(LinkedList<int[]> bodySnake1, LinkedList<int[]> bodySnake2) {
        LinkedList<int[]> bd1 = new LinkedList<int[]>(bodySnake1);
        LinkedList<int[]> bd2 = new LinkedList<int[]>(bodySnake2);

        int[] head1 = bd1.removeFirst();
        // System.out.println("head 1 " + head1[0] + " " + head1[1]);
        int[] head2 = bd2.removeFirst();
        // System.out.println("head 2 " + head1[0] + " " + head1[1]);

        if (head1[0] == head2[0] && head1[1] == head2[1]) {
            this.snake2Collision = true;
            this.snake1Collision = true;
            if (snake1.applesEaten > snake2.applesEaten) {
                this.Isplayer1Win = true;
            } else if (snake1.applesEaten < snake2.applesEaten) {

                this.Isplayer2Win = true;
            } else {
                this.Isplayer1Win = true;
                this.Isplayer2Win = true;

            }
            databaseAccess.updatePlayerScore(snake1.applesEaten, snake1.playerName);
            databaseAccess.updatePlayerScore(snake2.applesEaten, snake2.playerName);
            System.out.println("player 1 and player 2 collision");
        }

        for (int[] part1 : bd1) {
            if (head2[0] == part1[0] && head2[1] == part1[1]) {
                this.snake2Collision = true;
                this.Isplayer1Win = true;
                databaseAccess.updatePlayerScore(snake1.applesEaten, snake1.playerName);
                databaseAccess.updatePlayerScore(snake2.applesEaten, snake2.playerName);
                System.out.println("player 2 collision");

            }
        }
        for (int[] part2 : bd2) {
            if (head1[0] == part2[0] && head1[1] == part2[1]) {
                this.snake1Collision = true;
                this.Isplayer2Win = true;
                databaseAccess.updatePlayerScore(snake1.applesEaten, snake1.playerName);
                databaseAccess.updatePlayerScore(snake2.applesEaten, snake2.playerName);
                System.out.println("player 1 collision");

            }
        }
    }

    public void handlePlayerDataQueue(ArrayList<ClientHandler> clientHandlers) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (ClientHandler.getListclientHandlersSize() >= 2) {
                        GameStatePacket packetGameState;
                        PlayerDataPacket p1;
                        PlayerDataPacket p2;
                        try {
                            p1 = ClientHandler.clientHandlers.get(0).popHead_Queue_playerData();
                        } catch (Exception e) {
                            // TODO: handle exception
                            p1 = null;
                        }
                        try {
                            p2 = ClientHandler.clientHandlers.get(1).popHead_Queue_playerData();
                        } catch (Exception e) {
                            // TODO: handle exception
                            p2 = null;

                        }
                        packetGameState = calculateGameState(p1, p2);
                        if (packetGameState == null) {
                            continue;
                        } else {
                            for (ClientHandler clientHandler : clientHandlers) {
                                try {
                                    clientHandler.out.writeObject(packetGameState);
                                    clientHandler.out.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    // try {
                    // Thread.sleep(10);
                    // } catch (InterruptedException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // }
                }

            }
        }).start();
    }

    public void resetGame() {
        snake1Collision = false;
        snake2Collision = false;
        playerName1 = "";
        playerName2 = "";
        setup_snake(playerName1, playerName2);
        newApple();
        Isplayer1Win = false;
        Isplayer2Win = false;
        gameInitPacket = new GameInitPacket(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, GAME_UNITS, DELAY, playerName1,
                playerName2, data_snake1, data_snake2, appleX, appleY);
    }

}
