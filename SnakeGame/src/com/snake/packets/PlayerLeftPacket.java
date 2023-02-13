package com.snake.packets;

import java.io.Serializable;

public class PlayerLeftPacket implements Serializable {
    private static final long serialVersionUID = 1L;
    public String playerLeftName;
    public boolean isLeft;

    public PlayerLeftPacket(String name, boolean isLeft) {
        this.playerLeftName = name;
        this.isLeft = isLeft;
    }
}
