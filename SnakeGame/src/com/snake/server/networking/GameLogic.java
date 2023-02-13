package com.snake.server.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.snake.client.gui.Snake;
import com.snake.client.networking.SnakeGameplayData;
import com.snake.packets.GameInitPacket;
import com.snake.packets.GameStatePacket;
import com.snake.packets.PlayerDataPacket;

public class GameLogic {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 15;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 75;
    int appleX, appleY;
    private Random random;
    private GameStatePacket gameStatePacket;
    SnakeGameplayData data_snake1;
    SnakeGameplayData data_snake2;
    Snake snake1;
    Snake snake2;
    private String playerName1 = "";
    private String playerName2 = "";

    private GameInitPacket gameInitPacket;

    public GameLogic() {
        random = new Random();
        setup_snake(playerName1, playerName2);
        newApple();
        gameInitPacket = new GameInitPacket(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, GAME_UNITS, DELAY, playerName1,
                playerName2, data_snake1, data_snake2, appleX, appleY);
    }

    private void newApple() {
        this.appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        this.appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    private void setup_snake(String playerName1, String playerName2) {
        // snake player 1
        snake1 = new Snake(playerName1, GAME_UNITS, 3, 'R');
        snake1.setHead(0, 0);
        data_snake1 = new SnakeGameplayData(snake1);

        // snake player 2
        snake2 = new Snake(playerName2, GAME_UNITS, 3, 'L');
        snake2.setHead(39 * UNIT_SIZE, 39 * UNIT_SIZE);
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
                moveSnake(snake2);
                data_snake1 = new SnakeGameplayData(snake1);
                data_snake2 = new SnakeGameplayData(snake2);
                checkApple(appleX, appleY, data_snake1.body.getFirst(), data_snake2.body.getFirst());
                return new GameStatePacket(data_snake1, data_snake2, appleX, appleY);
            }
        } else {
            if (p2 == null) {
                snake1.direction = p1.direction;
                moveSnake(snake1);
                data_snake1 = new SnakeGameplayData(snake1);
                data_snake2 = new SnakeGameplayData(snake2);
                checkApple(appleX, appleY, data_snake1.body.getFirst(), data_snake2.body.getFirst());

                return new GameStatePacket(data_snake1, data_snake2, appleX, appleY);
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
        checkApple(appleX, appleY, data_snake1.body.getFirst(), data_snake2.body.getFirst());
        return new GameStatePacket(data_snake1, data_snake2, appleX, appleY);
    }

    public void moveSnake(Snake snake) {
        snake.pop();
        int x = snake.getHead()[0];
        int y = snake.getHead()[1];
        switch (snake.direction) {
            case 'U':
                snake.pushHead(x, y - UNIT_SIZE);
                break;
            case 'D':
                snake.pushHead(x, y + UNIT_SIZE);
                break;
            case 'L':
                snake.pushHead(x - UNIT_SIZE, y);
                break;
            case 'R':
                snake.pushHead(x + UNIT_SIZE, y);
                break;
        }
    }

    private void checkApple(int appleX, int appleY, int[] head_snake1, int[] head_snake2) {
        if (head_snake1[0] == appleX && head_snake1[1] == appleY) {
            snake1.applesEaten++;
            snake1.bodyParts++;
            newApple();
        } else if (head_snake2[0] == appleX && head_snake2[1] == appleY) {
            snake2.applesEaten++;
            snake2.bodyParts++;
            newApple();
        }
    }

    public void handlePlayerDataQueue(ArrayList<ClientHandler> clientHandlers) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // System.out.println(clientHandlers.size());
                    // if (clientHandlers.size() >= 2) {
                    if (ClientHandler.getListclientHandlersSize() >= 2) {
                        PlayerDataPacket p1 = ClientHandler.clientHandlers.get(0).popHead_Queue_playerData();
                        PlayerDataPacket p2 = ClientHandler.clientHandlers.get(1).popHead_Queue_playerData();
                        GameStatePacket packetGameState = calculateGameState(p1, p2);

                        if (packetGameState == null) {
                            continue;
                        } else {
                            for (ClientHandler clientHandler : clientHandlers) {
                                try {
                                    clientHandler.out.writeObject(packetGameState);
                                    clientHandler.out.flush();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

}
