import javax.swing.*;

public class GameFrame {
    private JFrame gameFrame;
    private KeyBinding keyBinding;
    public GameFrame(Client client, String username) {
        this.gameFrame = new JFrame("snake");
        JRootPane rootPane = gameFrame.getRootPane();
        keyBinding = new KeyBinding(rootPane, username, client);
        gameFrame.setSize(400,400);
        gameFrame.setLayout(null);
        gameFrame.setVisible(true);
    }

}
