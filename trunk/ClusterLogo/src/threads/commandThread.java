/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package threads;

import data.hostInfoStorage;
import data.runConfig;
import gui.hostGui;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Command Thread sends out assignments to clients
 *
 * @author Todd Bodnar
 */
public class commandThread implements Runnable{

    /**
     * Sets up thread, initializes server
     * @param storage the hostInfoStorage that keeps track of the assignments
     * @param myGui the Server's gui
     * @throws IOException if the serverSocket cannot be set up
     */
    public commandThread(hostInfoStorage storage, hostGui myGui) throws IOException
    {
        this.storage = storage;
        this.myGui = myGui;
        server = new ServerSocket(3010,128);
    }

    public void run() {
        while(true)//keep repeating the process until the program is terminated
        {
            while(storage.getTodoSize()==0) //while there are no more projects to send
            {
                try {
                    Thread.sleep(10000);  //wait 10 seconds to see if new ones are added
                } catch (InterruptedException ex) {
                }
            }
            try {
                Socket connection = server.accept();//Get a connection
                runConfig assignment = storage.getProject();//Get a project
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                out.writeObject(assignment);//Send the project
                tools.fileTransfer.sendFile(assignment.getNLogo(), out, myGui.getUploadBar());//send the nLogo file
                out.close();

                myGui.getUploadBar().setString("inactive");

                storage.moveFromTodoToInProgress(assignment);
                myGui.updateView();
            } catch (IOException ex) {
                Logger.getLogger(commandThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private hostInfoStorage storage;
    private hostGui myGui;
    private ServerSocket server;
}
