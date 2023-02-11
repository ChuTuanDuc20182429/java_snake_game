package com.snake.client.networking;

import com.snake.packets.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public String clientUsername;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in ;
    private EventListener listener;
    public Boolean waitFlag;
    public Client(Socket socket, String username) {
        try {
            // Set up the connection
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.listener = new EventListener();

            // Send packet right after login to the game
            AddConnectionPacket addConnectionPacket = new AddConnectionPacket(username);
            this.out.writeObject(addConnectionPacket);
            this.out.flush();
            // Listen for waitFlag from server
            this.waitFlag = true;
            while (waitFlag) {
                Object p = this.in.readObject();
                listener.received(p, this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendPacket(PlayerDataPacket packet) {
        try {
            if(socket.isConnected()) {
                out.writeObject(packet);
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
                PlayerDataPacket packet;
                try {
                    while (socket.isConnected()) {
                        packet = (PlayerDataPacket) in.readObject();
                        System.out.println(packet.username);
                        System.out.println(packet.direction);
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
