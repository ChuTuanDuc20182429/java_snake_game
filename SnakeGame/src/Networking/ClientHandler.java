package Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static volatile ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientUsername;
    // private EventListener listener;
    private Packet packet;

    public ClientHandler(Socket socket) {
        try {
            // Set up the connection
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream((socket.getInputStream()));

            this.packet = (Packet) this.in.readObject();
            this.clientUsername = this.packet.username;
            clientHandlers.add(this); // put this in EventListener

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
                packetFromClient = (Packet) in.readObject(); // listen for packet
                broadcastPacket(packetFromClient); // send packet
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
