import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientUsername;
    // private EventListener listener;
    private Packet packet;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream((socket.getInputStream()));
            this.packet = (Packet) this.in.readObject();
            this.clientUsername = this.packet.username;
            //add new client to the clientHandlers list
            clientHandlers.add(this);
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
                packetFromClient = (Packet) in.readObject();
                broadcastPacket(packetFromClient);

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