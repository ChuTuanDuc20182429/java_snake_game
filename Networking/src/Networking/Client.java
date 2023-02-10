package Networking;

import java.io.*;
import java.net.Socket;

public class Client {
    // private JFrame gameFrame;
    private Socket socket;
    public ObjectOutputStream out;
    public ObjectInputStream in;
    private Packet packet;
    public String clientUsername;
    public boolean ready;
    // private String keyCode;
    public Packet receivedPacket;
    public boolean setup = false;
    public Packet setup_Packet;
    public char snake_remote_direction;

    public Client(Socket socket, String username) {
        try {
            this.setup_Packet = new Packet();
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.clientUsername = username;

            // Send packet with username and request for play right after login to the game
            this.packet = new Packet(username, true);
            sendPacket(packet);
            // this.out.writeObject(this.packet);
            // this.out.flush();

            this.receivedPacket = receivedPacket();

        } catch (IOException e) {
            e.printStackTrace();
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

    public synchronized Packet receivedPacket() {
        try {
            receivedPacket = (Packet) in.readObject();

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return receivedPacket;

    }

    public synchronized void update_snake2_direction(char direction) {
        snake_remote_direction = direction;
    }

    public synchronized char get_snake2_direction() {
        return snake_remote_direction;
    }

    public void listenForPacket() {
        new Thread(new Runnable() {
            // Packet packet_from_sever;

            @Override
            public void run() {
                while (socket.isConnected()) {
                    Packet p = receivedPacket();
                    if (p == null) {
                        System.out.println("p null");
                        continue;
                    }
                    if (p.ready_to_play) {
                        System.out.println("ready = true");
                        ready = true;
                    } else if (p.setup) {
                        setup_Packet = receivedPacket;
                        System.out.println("setup = true");
                        setup = true;

                    } else if (p.command) {
                        update_snake2_direction(p.direction_snake2);
                        System.out.println(receivedPacket.username);
                        System.out.println("received: " + receivedPacket.direction_snake2);
                        System.out.println("ready or not: " + receivedPacket.ready_to_play);
                        System.out.println(receivedPacket.n_players);
                    }

                }
            }
        }).start();
    }
}
