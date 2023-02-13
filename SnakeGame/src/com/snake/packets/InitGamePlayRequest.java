package com.snake.packets;

import java.io.Serializable;

public class InitGamePlayRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean request = false;

    public InitGamePlayRequest(boolean request) {
        this.request = request;
    }

}
