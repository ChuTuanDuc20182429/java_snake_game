import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard extends JPanel {
    public static int keyCode;
    public Keyboard() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                System.out.println("Key typed: " + keyEvent.getKeyCode());
                keyCode = keyEvent.getKeyCode();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                System.out.println("Key pressed: " + keyEvent.getKeyCode());
                keyCode = keyEvent.getKeyCode();
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                System.out.println("Key released: " + keyEvent.getKeyCode());
                keyCode = keyEvent.getKeyCode();
            }
        });

    }
}
