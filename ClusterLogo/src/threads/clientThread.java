/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package threads;

import data.runConfig;
import gui.clientGuiPerThread;
import gui.clientGuiRunner;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.nlogo.api.CompilerException;
import org.nlogo.api.LogoException;
import org.nlogo.headless.HeadlessWorkspace;

/**
 * Thread run by the client
 *
 * @todo Clean up, break into smaller functions
 * @todo Test thread
 * @author Todd Bodnar
 */
public class clientThread implements Runnable{

    /**
     * Initializes the client's thread. Each client may run multiple clientThreads
     * @param threadGui The gui specific to this thread
     * @param clientGui The general gui for the client
     * @param tempFolder The folder where the simulation will be run, outputed, and compressed
     * @param hostIp The IP address of the host
     */
    public clientThread(clientGuiPerThread threadGui, clientGuiRunner clientGui, File tempFolder, String hostIp)
    {
        this.threadGui = threadGui;
        this.runnerGui = clientGui;
        this.workingFolder = tempFolder;
        this.hostIP = hostIp;
        workingFolder.mkdir();//make the folder at workingFolder
        //System.out.println(workingFolder);
    }

    public void run() {
        while(!runnerGui.noMoreBoxChecked())//keep running until the user no longer wants to run any more experiments from this client
        {
            threadGui.setMessageText("Waiting for connection");
            try {
                ObjectInputStream in = null;
                while(in==null)//while contact has not been able to be made (needed incase the server goes offline, times out, etc
                {
                    try
                    {
                    client = new Socket(hostIP, 3010);
                    in = new ObjectInputStream(client.getInputStream());
                    }
                    catch(Exception e)
                    {
                        threadGui.setMessageText(e.toString());
                    }
                }
                runConfig assignment = (runConfig) in.readObject();//load the runConfig

                File location = new File(workingFolder.toString()+File.separatorChar+"executeMe.nLogo");

                threadGui.setMessageText("Downloading executable");

                tools.fileTransfer.getFile(location, in, threadGui.getTransferBar());//download the .nLogo file

                in.close();
                client.close();

                threadGui.setMessageText("Configuring file");

                final HeadlessWorkspace app = HeadlessWorkspace.newInstance();//start NetLogo in headless mode
                app.open(location.toString());//open the nLogo file
                long length;
                length = assignment.getLength();
                for(int ct=0;ct<assignment.getCommands().size();ct++)//for the number of commands
                     app.command("set "+assignment.getCommands().elementAt(ct));//set the commands
                app.command("setup");

                threadGui.setMessageText("Running Experiment");
                for(long ct=0;ct<length;ct++)//for each tick
                {
                    if(ct%(length/100+1)==0)//check for roughly being a percentage point in (the +1 is needed to avoid divided by zero issues when length<100
                        threadGui.getCurrentBar().setValue((int) (100 * ct / length));//update the status bar
                    app.command("go");//press the go button
                }

                threadGui.getCurrentBar().setValue(100);

                app.dispose();

                threadGui.setMessageText("Compressing Data");
                // base on http://java.sun.com/developer/technicalArticles/Programming/compression/
                File dataFileTmp = new File(workingFolder.toString()+File.separatorChar+"data.dat");
                ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(dataFileTmp.toString()+".zip")));

                byte buffer[] = new byte[4096];
                BufferedInputStream zin = new BufferedInputStream(new FileInputStream(dataFileTmp),4096);


                zout.putNextEntry(new ZipEntry(dataFileTmp.toString()));
                int count;
                while((count = zin.read(buffer, 0,4096)) != -1) {
                   zout.write(buffer, 0, count);
                }

                zout.flush();
                zout.close();
                zin.close();


                ObjectOutputStream out=null;

                threadGui.setMessageText("Waiting to upload file");
                while(out==null){//see above on why these loops are necessary
                    try{
                     client = new Socket(hostIP,3011);
                     out = new ObjectOutputStream(client.getOutputStream());
                    }catch(Exception e){threadGui.setMessageText("Still waiting to connect: "+e.toString());}
                }
                threadGui.setMessageText("Uploading file");
                File dataFile = new File(workingFolder.toString()+File.separatorChar+"data.dat.zip");
                System.out.println(dataFile);
                while(!client.isClosed())
                {
                    try
                    {
                    out.writeObject(assignment);//upload the assignment
                    tools.fileTransfer.sendFile(dataFile, out, threadGui.getTransferBar());//upload the compressed file

                    out.close();
                    client.close();
                    }
                    catch(Exception e)
                    {
                        threadGui.setMessageText(e.toString());
                    }
                }
                threadGui.setMessageText("Cleaning up");
                //Delete the temp files
                dataFile.delete();
                dataFileTmp.delete();
                location.delete();

                runnerGui.increaseDoneCount();

            }

            catch (UnknownHostException ex) {
                threadGui.setMessageText("Could not connect to host: "+ex.getMessage());
                break;
            } catch (IOException ex) {
                threadGui.setMessageText("IO Error: "+ex.getMessage());
                break;
            }
            catch (ClassNotFoundException ex) {
                    Logger.getLogger(clientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (CompilerException ex) {
                    Logger.getLogger(clientThread.class.getName()).log(Level.SEVERE, null, ex);
                } catch (LogoException ex) {
                    Logger.getLogger(clientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            catch(InterruptedException ex)
            {
                Logger.getLogger(clientThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if(runnerGui.noMoreBoxChecked())
            threadGui.setMessageText("Thread terminated by user");
    }

    private clientGuiPerThread threadGui;
    private clientGuiRunner runnerGui;
    private File workingFolder;
    private String hostIP;
    private Socket client;

}
