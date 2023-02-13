package com.snake.client.networking;

import com.snake.packets.*;

public class EventListener {
    public void received(Object p, Client client) {
        // if (p instanceof AddConnectionPacket) {
        // System.out.println("Received AddConnectionPacket");

        // AddConnectionPacket addConnectionPacket = (AddConnectionPacket) p;

        // } else if (p instanceof RemoveConnectionPacket) {
        // System.out.println("Received RemoveConnectionPacket");

        // RemoveConnectionPacket packet = (RemoveConnectionPacket) p;

        // } else
        if (p instanceof NotifyPacket) {
            NotifyPacket packet = (NotifyPacket) p;

            if (packet.waitFlag) { // If waitFlag == true
                client.waitFlag = packet.waitFlag;
            } else {
                client.waitFlag = packet.waitFlag;
            }
        } else if (p instanceof GameStatePacket) {
            GameStatePacket packet = (GameStatePacket) p;
            System.out.println("recieved GameStatePacket packet");
            client.pushQueue(packet);
        } else if (p instanceof PlayerDataPacket) {
            PlayerDataPacket packet = (PlayerDataPacket) p;
            System.out.println("player name: " + packet.username);
            System.out.println("direction: " + packet.direction);
        } else if (p instanceof GameInitPacket) {
            GameInitPacket packet = (GameInitPacket) p;
            client.setGameInitPacket(packet);
        }

    }

}
