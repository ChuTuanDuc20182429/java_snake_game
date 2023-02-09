package Networking;

import Game.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;

public class LoginFrame {
    public static int n_players = 0;

    public static void main(String[] args) {
        JFrame f = new JFrame("Enter Username");
        final JTextField tf = new JTextField();
        tf.setBounds(50, 50, 150, 20);
        JButton b = new JButton("PLay");
        b.setBounds(50, 100, 95, 30);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    f.dispose();
                    Socket socket = new Socket("localhost", 1234);
                    Client client = new Client(socket, tf.getText());
                    System.out.println(client.receivedPacket.n_players);

                    // while (client.receivedPacket.n_players <= 1) {
                    // // System.out.println(client.receivedPacket.n_players);
                    // }
                    if (client.receivedPacket.ready_to_play) {
                        System.out.println("client.receivedPacket.n_players: " + client.receivedPacket.n_players);
                        client.listenForPacket();
                        new GameFrame(client);

                    } else {
                        System.out.println(client.receivedPacket.ready_to_play);
                        System.out.println("wait for players");
                        client.listenForPacket();
                        while (!client.receivedPacket.ready_to_play) {
                            try {
                                Thread.sleep(1000);
                                System.out.println("in while " + client.receivedPacket.ready_to_play);
                            } catch (InterruptedException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }

                        new GameFrame(client);
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        f.add(b);
        f.add(tf);
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
    }
}
