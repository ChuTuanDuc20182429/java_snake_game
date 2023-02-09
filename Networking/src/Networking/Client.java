package Networking;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    // private JFrame gameFrame;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Packet packet;
    public String clientUsername;
    // private String keyCode;
    Packet receivedPacket;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.clientUsername = username;

            // Send packet with username and request for play right after login to the game
            this.packet = new Packet(username);
            this.out.writeObject(this.packet);
            this.out.flush();

            this.receivedPacket = (Packet) this.in.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPacket(Packet packet1) {
        try {
            if (socket.isConnected()) {
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
                try {
                    while (socket.isConnected()) {
                        receivedPacket = (Packet) in.readObject();
                        System.out.println(receivedPacket.username);
                        System.out.println(receivedPacket.keycode);
                        System.out.println(receivedPacket.ready_to_play);
                        System.out.println("received: " + receivedPacket.keycode);
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
