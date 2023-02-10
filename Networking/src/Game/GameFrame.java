package Game;

import Networking.Client;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(Client client) {
        this.add(new GamePanel(client));

        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
