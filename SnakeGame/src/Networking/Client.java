package Networking;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
//    private JFrame gameFrame;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in ;
    private Packet packet;
    public String clientUsername;
    private String keyCode;
    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.clientUsername = username;

            // Send packet right after login to the game
            this.packet = new Packet(username, 1, 0);
            this.out.writeObject(this.packet);
            this.out.flush();

            // Listen for waitFlag from server
            this.in.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendPacket(Packet packet1) {
        try {
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
}
