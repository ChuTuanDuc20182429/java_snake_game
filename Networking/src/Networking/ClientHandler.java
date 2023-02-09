package Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.text.PlainDocument;

public class ClientHandler implements Runnable {
    public static volatile ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public volatile static int number_players;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientUsername;
    private boolean playing = false;
    // private EventListener listener;
    private Packet packet;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream((socket.getInputStream()));
            this.packet = (Packet) this.in.readObject();
            if (packet.request == true) {
                this.clientUsername = this.packet.username;
                System.out.println("PLayer sent name");
                // add new client to the clientHandlers list
                clientHandlers.add(this);
                number_players++;
                if (number_players >= 2) {
                    Packet pac = new Packet(this.clientUsername, true, number_players);
                    this.out.writeObject(pac);
                    this.out.flush();
                    playing = true;
                } else {
                    Packet pac = new Packet(this.clientUsername, false, number_players);
                    this.out.writeObject(pac);
                    this.out.flush();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Packet packetFromClient;
        while (socket.isConnected()) {
            try {
                while (playing == false) {
                    if (number_players >= 2) {
                        Packet pac = new Packet(this.clientUsername, true, number_players);
                        this.out.writeObject(pac);
                        this.out.flush();
                        System.out.println("sen ready on run client handler");
                        playing = true;
                    }

                }
                packetFromClient = (Packet) in.readObject();
                packetFromClient.ready_to_play = true;
                broadcastPacket(packetFromClient);
                // if (packetFromClient.request) {
                // }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastPacket(Packet packet) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.out.writeObject(packet);
                    clientHandler.out.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
