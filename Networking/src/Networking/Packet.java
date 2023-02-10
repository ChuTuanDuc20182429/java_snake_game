package Networking;

import java.io.Serializable;

public class Packet implements Serializable {
    private final long serialVersionUID = 1L;
    public int keycode;
    public Boolean collision;
    public int n_players;
    public int appleEatenX, appleEatenY;
    public String username;
    public boolean request;
    public boolean ready_to_play;
    public int init_posx_snake1;
    public int init_posy_snake1;
    public int init_posx_snake2;
    public int init_posy_snake2;
    public char direction_snake1;
    public char direction_snake2;
    public boolean setup;
    public boolean command;

    public Packet() {
    }

    public Packet(String username, int keyCode, int n_players) {
        this.username = username;
        this.keycode = keyCode;
        this.n_players = n_players;
    }

    public Packet(String username, boolean request) {
        this.username = username;
        this.request = request;
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

    public Packet(String username, int posx_snake1, int posy_snake1, int posx_snake2, int posy_snake2,
            char direction_snake1, char direction_snake2,
            boolean setup) {
        this.username = username;
        this.init_posx_snake1 = posx_snake1;
        this.init_posy_snake1 = posy_snake1;
        this.init_posx_snake2 = posx_snake2;
        this.init_posy_snake2 = posy_snake2;
        this.setup = setup;
        this.direction_snake1 = direction_snake1;
        this.direction_snake2 = direction_snake2;
    }

    public Packet(String username, char direction, boolean command) {
        this.username = username;
        this.direction_snake2 = direction;
        this.command = command;
    }

}
