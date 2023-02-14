package com.snake.client.networking;

import com.snake.packets.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.LinkedList;

public class Client {
    public String clientUsername;
    private Socket socket;
    public ObjectOutputStream out;
    public ObjectInputStream in;
    private EventListener listener;
    public Boolean waitFlag;
    private Queue<GameStatePacket> GameState_queue;
    private GameInitPacket gameInitPacket;
    public boolean isWinner;

    public Client(Socket socket, String username) {
        try {
            // Set up the connection
            this.clientUsername = username;
            this.GameState_queue = new LinkedList<>();
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
        // System.out.println("send command");
        try {
            if (socket.isConnected()) {
                out.writeObject(packet);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInitRequest() {
        try {
            InitGamePlayRequest rqs = new InitGamePlayRequest(true);
            if (socket.isConnected()) {
                out.writeObject(rqs);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Client getClient() {
        return this;
    }

    public void pushQueue(GameStatePacket e) {
        GameState_queue.add(e);
    }

    public GameStatePacket popQueue() {
        return GameState_queue.poll();
    }

    public synchronized GameInitPacket getGameInitPacket() {
        return this.gameInitPacket;
    }

    public synchronized void setGameInitPacket(GameInitPacket p) {
        this.gameInitPacket = p;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void listenForPacket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object packet;
                try {
                    while (socket.isConnected()) {
                        packet = in.readObject();
                        listener.received(packet, getClient());
                    }
                } catch (IOException e) {
                    // e.printStackTrace();name);
                    // System.out.println("direction: " + packet.direction);
                    closeEveryThing(socket, out, in);
                } catch (ClassNotFoundException e) {
                    // e.printStackTrace();
                }

            }
        }).start();
    }

    public void closeEveryThing(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();

            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
