package com.snake.client.networking;

import java.io.Serializable;
import java.util.LinkedList;

import com.snake.client.gui.Snake;

public class SnakeGameplayData implements Serializable {
    public int bodyParts;
    public int applesEaten;
    public char direction;
    public String playerName;

    public LinkedList<int[]> body;

    public SnakeGameplayData(Snake snake) {
        this.playerName = snake.playerName;
        this.bodyParts = snake.bodyParts;
        this.applesEaten = snake.applesEaten;
        this.direction = snake.direction;
        this.body = new LinkedList<int[]>(snake.getBody());
        this.playerName = snake.playerName;
    }
}
