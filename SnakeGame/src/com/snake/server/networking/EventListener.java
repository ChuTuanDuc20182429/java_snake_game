package com.snake.server.networking;

import com.snake.packets.*;

import java.io.IOException;

public class EventListener {
    private String url = "jdbc:mysql://localhost:3306/HighScore";
    private String username = "debian-sys-maint";
    private String password = "CNCTEDLTOWKK1fFS";

    public void received(Object p, ClientHandler clientHandler) {
        if (p instanceof AddConnectionPacket) {
            AddConnectionPacket packet = (AddConnectionPacket) p;
            System.out.println("Received AddConnectionPacket");
            clientHandler.clientUsername = packet.clientUsername;
            ClientHandler.clientHandlers.add(clientHandler);
            System.out.println(clientHandler.clientUsername + " has joined the game!");

            // check number of player
            while (ClientHandler.clientHandlers.size() <= 1) {
                // notify client to wait
                sendNotify(true, clientHandler);
            }
            System.out.println("Sent notifyPacket to " + clientHandler.clientUsername);
            DatabaseAccess databaseAccess = new DatabaseAccess(this.url, this.username, this.password);
            databaseAccess.addNewRecord(packet.clientUsername);
            sendNotify(false, clientHandler);

        } else if (p instanceof RemoveConnectionPacket) {
            RemoveConnectionPacket packet = (RemoveConnectionPacket) p;
            System.out.println("Received RemoveConnectionPacket");

            ClientHandler.clientHandlers.remove(clientHandler);

        } else if (p instanceof PlayerDataPacket) {
            PlayerDataPacket packet = (PlayerDataPacket) p;
            System.out.println("Received PlayerDataPacket from " + packet.username);
            clientHandler.pushTail_Queue_playerData(packet);
        } else if (p instanceof InitGamePlayRequest) {
            System.out.println("recieved init rqs");
            sendInitGameData(clientHandler, ClientHandler.clientHandlers.get(0).clientUsername,
                    ClientHandler.clientHandlers.get(1).clientUsername);
        }
    }

    public void sendNotify(Boolean waitFlag, ClientHandler clientHandler) {
        NotifyPacket notifyPacket = new NotifyPacket(waitFlag);
        try {
            clientHandler.out.writeObject(notifyPacket);
            clientHandler.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInitGameData(ClientHandler clientHandler, String playerName1, String playerName2) {
        try {
            Server.game.setPlayers_name(playerName1, playerName2);
            clientHandler.out.writeObject(Server.game.getGameInitPacket());
            clientHandler.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastPacket(PlayerDataPacket packet, ClientHandler clientHandler) {
        for (ClientHandler cli : ClientHandler.clientHandlers) {
            try {
                if (!cli.clientUsername.equals(clientHandler.clientUsername)) {
                    cli.out.writeObject(packet);
                    cli.out.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
