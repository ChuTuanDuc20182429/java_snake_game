package Networking;

import java.io.Serializable;

public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    public int keycode;
    public Boolean collision;
    public int n_players;
    public int appleEatenX, appleEatenY;
    public String username;
    public boolean request;

    public Packet(String username, int keyCode, int n_players) {
        this.username = username;
        this.keycode = keyCode;
        this.n_players = n_players;
        this.request = true;
    }
}
