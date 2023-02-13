package com.snake.packets;

import java.io.Serializable;

public class PlayerDataPacket implements Serializable {
    private static final long serialVersionUID = 1L;
    public String username;
    public char direction;

    public PlayerDataPacket(String username, char direction) {
        this.username = username;
        this.direction = direction;
    }
}
