import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in ;
    public Packet packet;
    private int keycode;
    private String clientUsername;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;

    public Client(Socket socket, String username, int keyCode) {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.packet = new Packet(username, 100, keyCode);
            this.clientUsername = username;

            // Populate the packet

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendPacket() {
        try {
            dataOut = new DataOutputStream(socket.getOutputStream());

            while (socket.isConnected()) {
                keycode = Keyboard.keyCode;
//                System.out.println("packet keycode: " + packet.keycode);

//                out.writeObject(packet);
//                out.flush();
                dataOut.writeInt(keycode);
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
                    dataIn = new DataInputStream(socket.getInputStream());
                    while (socket.isConnected()) {
                        keycode = dataIn.readInt();
                        packet1 = (Packet) in.readObject();
//                        System.out.println(packet1.username);
//                        System.out.println(packet1.score);
//                        System.out.println(packet1.keycode);
                        System.out.println("received: " + keycode);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public static void main(String[] args) throws IOException{

            Socket socket = new Socket("localhost", 1234);
            Client client = new Client(socket, "chutuanduc", 83);
            client.listenForPacket();
            client.sendPacket();

    }

}
