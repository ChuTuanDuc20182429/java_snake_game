package com.snake.packets;

import java.io.Serializable;

import com.snake.client.networking.SnakeGameplayData;

public class GameStatePacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public String player1;
    public String player2;
    public SnakeGameplayData snake1_data;
    public SnakeGameplayData snake2_data;

    public int appleX;
    public int appleY;

    private boolean snake1_collision = false;
    private boolean snake2_collision = false;
    public boolean Isplayer1Win = false;
    public boolean Isplayer2Win = false;

    public GameStatePacket(SnakeGameplayData snake1_data, SnakeGameplayData snake2_data, int appleX,
            int appleY, boolean snake1_collision, boolean snake2_collision, boolean Isplayer1Win,
            boolean Isplayer2Win) {

        this.appleX = appleX;
        this.appleY = appleY;
        this.snake1_data = snake1_data;
        this.snake2_data = snake2_data;
        this.snake1_collision = snake1_collision;
        this.snake2_collision = snake2_collision;
        this.Isplayer1Win = Isplayer1Win;
        this.Isplayer2Win = Isplayer2Win;

    }

    public void setCollisionStatus(boolean snake1_collision, boolean snake2_collision) {
        this.snake1_collision = snake1_collision;
        this.snake2_collision = snake2_collision;

    }

    public boolean getCollisionStatus_snake1() {
        return this.snake1_collision;
    }

    public boolean getCollisionStatus_snake2() {
        return this.snake2_collision;
    }
}
