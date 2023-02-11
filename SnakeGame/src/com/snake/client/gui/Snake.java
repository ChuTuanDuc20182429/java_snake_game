package com.snake.client.gui;

public class Snake {

    public int x[];
    public int y[];
    public int bodyParts;
    public int applesEaten;
    public char direction;

    public Snake(int GAME_UNIT, int bodyParts, char direction) {
        this.x = new int[GAME_UNIT];
        this.y = new int[GAME_UNIT];
        this.bodyParts = bodyParts;
        this.applesEaten = 0;
        this.direction = direction;

    }
}
