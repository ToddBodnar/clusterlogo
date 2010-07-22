package mains;

import data.hostInfoStorage;
import gui.hostGui;
import java.io.IOException;
import javax.swing.JOptionPane;
import threads.commandThread;
import threads.receiverThread;

/**
 * Entry point for the host (Server)
 * @author Todd Bodnar
 */
public class host {
    public static void main(String[] args)
    {
        hostInfoStorage infoStorage = new hostInfoStorage();
        hostGui gui = new hostGui();
        gui.setDataStorage(infoStorage);
        try {
            commandThread command = new commandThread(infoStorage, gui);
            receiverThread receive = new receiverThread(gui, infoStorage);

            Thread t1 = new Thread(command);
            Thread t2 = new Thread(receive);

            t1.start();
            t2.start();

            gui.setVisible(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex);
            System.exit(-1);
        }
    }
}
