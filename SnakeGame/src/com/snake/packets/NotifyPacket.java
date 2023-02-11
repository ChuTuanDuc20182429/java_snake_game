package com.snake.packets;

import java.io.Serializable;

public class NotifyPacket implements Serializable {
    private static final long serialVersionUID = 1L;
    public Boolean waitFlag;
    public NotifyPacket(Boolean waitFlag) {
        this.waitFlag = waitFlag;
    }
}
