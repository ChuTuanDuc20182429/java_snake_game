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
    public boolean ready_to_play;

    public Packet(String username, int keyCode, int n_players) {
        this.username = username;
        this.keycode = keyCode;
        this.n_players = n_players;
    }

    public Packet(String username) {
        this.username = username;
        this.request = true;
    }

    public Packet(String username, int keyCode) {
        this.username = username;
        this.keycode = keyCode;
    }

    public Packet(String username, boolean ready, int number_player) {
        this.username = username;
        this.ready_to_play = ready;
        this.n_players = number_player;
    }
}
