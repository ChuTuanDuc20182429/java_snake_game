import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
//    private JFrame gameFrame;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in ;
    private Packet packet;
    private String clientUsername;
//    private KeyBinding keyBinding;
    private String keyCode;
    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.clientUsername = username;

//            this.gameFrame = new JFrame("snake");
//            JRootPane rootPane = gameFrame.getRootPane();
//            keyBinding = new KeyBinding(rootPane);

//            rootPane.getInputMap().put(KeyStroke.getKeyStroke("A"), "leftAction");
//            rootPane.getActionMap().put("leftAction", keyBinding.leftAction);
//            rootPane.getInputMap().put(KeyStroke.getKeyStroke("D"), "rightAction");
//            rootPane.getActionMap().put("rightAction", keyBinding.rightAction);
//            rootPane.getInputMap().put(KeyStroke.getKeyStroke("W"), "upAction");
//            rootPane.getActionMap().put("upAction", keyBinding.upAction);
//            rootPane.getInputMap().put(KeyStroke.getKeyStroke("S"), "downAction");
//            rootPane.getActionMap().put("downAction", keyBinding.downAction);

            this.packet = new Packet(username, 100, 1);

//            gameFrame.setSize(400,400);
//            gameFrame.setLayout(null);
//            gameFrame.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendPacket(Packet packet1) {
        try {
//            while (socket.isConnected()) {
////                packet.keycode = keyBinding.getKeyCode();
//                out.writeObject(packet);
//
//                out.flush();
//                System.out.println("sent: " + 0);
//            }
            if(socket.isConnected()) {
                out.writeObject(packet1);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void listenForPacket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Packet packet1;
                try {
                    while (socket.isConnected()) {
                        packet1 = (Packet) in.readObject();
                        System.out.println(packet1.username);
                        System.out.println(packet1.score);
                        System.out.println(packet1.keycode);
                        System.out.println("received: " + packet1.keycode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
//    public static void main(String[] args) throws IOException{
//
//            Socket socket = new Socket("localhost", 1234);
//            Client client = new Client(socket, "chutuanduc");
//            client.listenForPacket();
//            client.sendPacket();
//
//    }

}
