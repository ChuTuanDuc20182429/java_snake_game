package com.snake.server.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public Socket socket;
    public ObjectOutputStream out;
    public ObjectInputStream in;
    public String clientUsername;
    private EventListener listener;

    public ClientHandler(Socket socket) {
        try {
            // Set up connection
            this.socket = socket;
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
                e.printStackTrace();
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
        }

    }
}

