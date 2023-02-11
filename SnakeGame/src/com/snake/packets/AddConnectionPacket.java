package com.snake.packets;

import java.io.Serializable;

public class AddConnectionPacket implements Serializable {
    private static final long serialVersionUID = 1L;
    public String clientUsername;
    public AddConnectionPacket(String clientUsername) {
        this.clientUsername = clientUsername;
    }
}
