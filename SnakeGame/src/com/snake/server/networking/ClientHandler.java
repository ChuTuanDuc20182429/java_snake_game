package com.snake.server.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.snake.packets.PlayerDataPacket;
import com.snake.packets.PlayerLeftPacket;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Queue<PlayerDataPacket> playerData_queue;

    public Socket socket;
    public ObjectOutputStream out;
    public ObjectInputStream in;
    public String clientUsername;
    private EventListener listener;
    public static Server server;

    public ClientHandler(Socket socket, Server server) {
        try {
            // Set up connection
            this.socket = socket;
            ClientHandler.server = server;
            playerData_queue = new LinkedList<>();
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream((socket.getInputStream()));
            this.listener = new EventListener();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // ClientHandler is instantiated before Client, and wait for input from client
            Object p = this.in.readObject();
            listener.received(p, this);
            while (socket.isConnected()) {
                Object p1 = in.readObject();
                listener.received(p1, this);
            }
        } catch (IOException e) {
            try {
                this.in.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.out.println("socket can't close");
                // e1.printStackTrace();
            }
            // e.printStackTrace();
            closeEveryThing(socket, out, in);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void pushTail_Queue_playerData(PlayerDataPacket p) {
        this.playerData_queue.add(p);
    }

    public PlayerDataPacket popHead_Queue_playerData() {
        return this.playerData_queue.poll();
    }

    public static synchronized int getListclientHandlersSize() {
        return clientHandlers.size();
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage();

    }

    public void broadcastMessage() {
        System.out.println(clientUsername + " have left");
        PlayerLeftPacket packet = new PlayerLeftPacket(clientUsername, true);
        for (ClientHandler cli : ClientHandler.clientHandlers) {
            try {
                cli.out.writeObject(packet);
                cli.out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeEveryThing(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        removeClientHandler();
        Server.game.resetGame();
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
