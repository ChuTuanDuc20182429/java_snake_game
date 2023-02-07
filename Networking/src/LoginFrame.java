import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;
public class LoginFrame {
    public static void main(String[] args) {
    JFrame f = new JFrame("Button Example");
    Keyboard keyboard = new Keyboard();
    final JTextField tf = new JTextField();
    tf.setBounds(50,50, 150,20);
    JButton b = new JButton("Click Here");
    b.setBounds(50,100,95,30);
    b.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)  {
            try {

                JFrame gameFrame = new JFrame("snake");
                gameFrame.add(keyboard);

                gameFrame.setSize(400,400);
                gameFrame.setLayout(null);
                gameFrame.setVisible(true);

                Socket socket = new Socket("localhost", 1234);
                Client client = new Client(socket, tf.getText(), Keyboard.keyCode);

                client.listenForPacket();
                client.sendPacket();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    });
    f.add(b);f.add(tf);
    f.setSize(400,400);
    f.setLayout(null);
    f.setVisible(true);
}
}
