/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package threads;

import data.hostInfoStorage;
import data.runConfig;
import gui.hostGui;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Receives completed data files from the clients
 * @author Todd Bodnar
 */
public class receiverThread implements Runnable{

    /**
     * Sets up the ServerSocket and decompresser thread
     * @param myGui The hostGui
     * @param storage the hostInfoStorage
     * @throws IOException if the ServerSocket cannot be started
     */
    public receiverThread(hostGui myGui, hostInfoStorage storage) throws IOException
    {
        this.myGui = myGui;
        this.storage = storage;
        server = new ServerSocket(3011,128);

        decompresser = new decompressThread(myGui);
        Thread t = new Thread(decompresser);
        t.setPriority(Thread.MIN_PRIORITY);//the host needs to have more speed to the receiver and commander than the decompresser
        t.start();
    }

    public void run() {
        while(true)
        {
            try {
                Socket connection = server.accept();
                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                
                runConfig assignment = (runConfig) in.readObject();

                //---- choose a file that does not already exist to save to
                File f = new File("temp.0");
                int ct=0;
                while(f.exists())
                {
                    ct++;
                    f = new File("temp."+ct);
                }

                tools.fileTransfer.getFile(f, in, myGui.getDownloadBar());//save the file

                decompresser.add(assignment, f);
                
                storage.moveFromInProgressToCompleted(assignment);

                myGui.getDownloadBar().setString("inactive");
                myGui.updateView();
            } catch (IOException ex) {
               // JOptionPane.showMessageDialog(null,ex.toString());
            }  catch (ClassNotFoundException ex) {
                   //JOptionPane.showMessageDialog(null,ex.toString());
                }

        }
    }
    private hostGui myGui;
    private hostInfoStorage storage;
    private ServerSocket server;
    private decompressThread decompresser;
}
