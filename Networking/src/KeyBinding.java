import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.Socket;

public class KeyBinding {
    public Action leftAction;
    public Action rightAction;
    public Action upAction;
    public Action downAction;
    public String keyCode;
    Client client;
    String username;
//
    public KeyBinding(JRootPane rootPane, String username, Client client) {
//
        leftAction = new LeftAction();
        rightAction = new RightAction();
        upAction = new UpAction();
        downAction = new DownAction();
        this.client = client;
        this.username = username;

        rootPane.getInputMap().put(KeyStroke.getKeyStroke("A"), "leftAction");
        rootPane.getActionMap().put("leftAction", leftAction);
        rootPane.getInputMap().put(KeyStroke.getKeyStroke("D"), "rightAction");
        rootPane.getActionMap().put("rightAction", rightAction);
        rootPane.getInputMap().put(KeyStroke.getKeyStroke("W"), "upAction");
        rootPane.getActionMap().put("upAction", upAction);
        rootPane.getInputMap().put(KeyStroke.getKeyStroke("S"), "downAction");
        rootPane.getActionMap().put("downAction", downAction);


    }
    private class LeftAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            Packet p = new Packet(username, 100, KeyEvent.VK_A);
            client.sendPacket(p);
//            label.setLocation(label.getX() - 5, label.getY());

        }
    }

    private class RightAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            Packet p = new Packet(username, 100, KeyEvent.VK_D);
            client.sendPacket(p);
//            label.setLocation(label.getX() + 5, label.getY());

        }
    }

    private class UpAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            Packet p = new Packet(username, 100, KeyEvent.VK_W);
            client.sendPacket(p);
//            label.setLocation(label.getX(), label.getY() - 5);

        }
    }

    private class DownAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            Packet p = new Packet(username, 100, KeyEvent.VK_S);
            client.sendPacket(p);
//            label.setLocation(label.getX(), label.getY() + 5);

        }
    }
    public String getKeyCode() {
        return keyCode;
    }

//    public static void main(String[] args) {
//        JFrame myFrame;
//        myFrame = new JFrame();
//        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        myFrame.setSize(800, 600);
//        myFrame.setLayout(null);
//        JRootPane rootPane = myFrame.getRootPane();
//        myFrame.setVisible(true);
//
//
//        KeyBinding keyBinding = new KeyBinding(rootPane);
//        while(true) {
//            System.out.println(keyBinding.getKeyCode());
//        }
//    }
}
