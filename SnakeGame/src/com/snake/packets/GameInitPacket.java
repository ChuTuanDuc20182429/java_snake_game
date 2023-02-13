package com.snake.packets;

import java.io.Serializable;

import com.snake.client.networking.SnakeGameplayData;

public class GameInitPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;
    public int UNIT_SIZE;
    public int GAME_UNITS;
    public int DELAY;
    public String player1;
    public String player2;
    public SnakeGameplayData snake1_data;
    public SnakeGameplayData snake2_data;

    public int appleX;
    public int appleY;

    public GameInitPacket(int SCREEN_WIDTH, int SCREEN_HEIGHT, int UNIT_SIZE, int GAME_UNITS,
            int DELAY, String player1, String player2, SnakeGameplayData snake1_data, SnakeGameplayData snake2_data,
            int appleX, int appleY) {
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
        this.UNIT_SIZE = UNIT_SIZE;
        this.GAME_UNITS = GAME_UNITS;
        this.DELAY = DELAY;
        this.player1 = player1;
        this.player2 = player2;
        this.snake1_data = snake1_data;
        this.snake2_data = snake2_data;
        this.appleX = appleX;
        this.appleY = appleY;
    }

}
