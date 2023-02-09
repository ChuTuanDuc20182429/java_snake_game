package Networking;

import java.io.Serializable;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    public int keycode;
    public Boolean collision;
    public int appleEatenX, appleEatenY;
    public String username;

    public Packet(String username, int keyCode) {
        this.username = username;
        this.keycode = keyCode;
    }
}
