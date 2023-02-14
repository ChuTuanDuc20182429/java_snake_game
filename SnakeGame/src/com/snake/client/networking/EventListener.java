package com.snake.client.networking;

import javax.crypto.Cipher;

import com.snake.client.gui.GamePanel;
import com.snake.packets.*;

public class EventListener {
    public void received(Object p, Client client) {

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
            // System.out.println("player name: " + packet.username);
            // System.out.println("direction: " + packet.direction);
        } else if (p instanceof GameInitPacket) {
            GameInitPacket packet = (GameInitPacket) p;
            client.setGameInitPacket(packet);
        } else if (p instanceof PlayerLeftPacket) {
            PlayerLeftPacket packet = (PlayerLeftPacket) p;
            if (packet.isLeft == true) {
                System.out.println(packet.playerLeftName + " is left: " + packet.isLeft);
                client.isWinner = true;
                GamePanel.running = false;
            }
        }

    }

}
