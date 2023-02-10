package Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static int number_players;
    private static boolean setup_all_complete = false;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public String clientUsername;
    private boolean playing = false;
    private boolean setup = false;
    // private EventListener listener;
    private Packet packet;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream((socket.getInputStream()));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        clientHandlers.add(this);
    }

    public synchronized Packet readPacket(ObjectInputStream in_stream, Packet p) {
        try {
            p = (Packet) in_stream.readObject();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return p;
    }

    public synchronized void sendPacket(ObjectOutputStream out_stream, Packet p) {
        try {
            out_stream.writeObject(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Packet packetFromClient = new Packet();
        packetFromClient = readPacket(in, packetFromClient);
        if (packetFromClient.request == true) {
            this.clientUsername = packetFromClient.username;
            System.out.println("PLayer sent name");
            number_players++;

            // add new client to the clientHandlers list

            if (number_players >= 2) {
                Packet pac = new Packet(this.clientUsername, true, number_players);
                sendPacket(out, pac);
                // this.out.writeObject(pac);
                // this.out.flush();
                playing = true;
            } else {
                Packet pac = new Packet(this.clientUsername, false, number_players);
                sendPacket(out, pac);

                // this.out.writeObject(pac);
                // this.out.flush();
            }
        }

        while (socket.isConnected())

        {
            while (playing == false) {
                System.out.println(number_players);
                if (number_players >= 2) {
                    Packet pac = new Packet(this.clientUsername, true, number_players);
                    sendPacket(out, pac);
                    // out.writeObject(pac);
                    // out.flush();
                    System.out.println("send ready on run client handler");
                    playing = true;
                }
            }
            if (!setup_all_complete) {
                for (int i = 0; i < clientHandlers.size(); i++) {
                    System.out.println("clientHandlers.size() " + clientHandlers.size());
                    if (i == 0 && clientHandlers.get(i).setup == false) {
                        System.out.println("i " + i);
                        Packet p = new Packet(clientUsername, 0, 0, 30, 30, 'R', 'L', true);
                        clientHandlers.get(i).sendPacket(clientHandlers.get(i).out, p);

                        Packet pac = new Packet();
                        pac = (Packet) clientHandlers.get(i).readPacket(clientHandlers.get(i).in, pac);
                        System.out.println("recieve packet setup ");
                        if (pac.setup) {
                            clientHandlers.get(i).setup = true;
                            System.out.println("clientHandlers.get(i).setup = true");

                        }
                    }
                    if (i == 1 && clientHandlers.get(i).setup == false) {
                        System.out.println("i " + i);
                        Packet p = new Packet(clientUsername, 30, 30, 0, 0, 'L', 'R', true);
                        clientHandlers.get(i).sendPacket(clientHandlers.get(i).out, p);

                        Packet pac = new Packet();
                        pac = (Packet) clientHandlers.get(i).readPacket(clientHandlers.get(i).in, pac);
                        System.out.println("recieve packet setup ");

                        if (pac.setup) {
                            clientHandlers.get(i).setup = true;
                            System.out.println("clientHandlers.get(i).setup = true");
                        }
                    }
                }
                int count = 0;

                for (int a = 0; a < clientHandlers.size(); a++) {
                    if (clientHandlers.get(a).setup) {
                        count++;
                        System.out.println("count " + count);
                    }
                }
                if (count == (clientHandlers.size())) {
                    System.out.println("setup_all_complete = true;");
                    setup_all_complete = true;
                }

            } else {
                packetFromClient = readPacket(in, packetFromClient);
                // packetFromClient.ready_to_play = true;
                System.out.println("broard cast");
                broadcastPacket(packetFromClient);
            }
        }
    }

    public void broadcastPacket(Packet packet) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.clientUsername.equals(clientUsername)) {
                clientHandler.sendPacket(clientHandler.out, packet);

                // clientHandler.out.writeObject(packet);
                // clientHandler.out.flush();
            }
        }
    }
}
