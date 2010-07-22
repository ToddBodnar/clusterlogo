/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package threads;

import data.runConfig;
import gui.hostGui;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

/**
 * Decompresses files in the thread's queue
 * @author Todd Bodnar
 */
public class decompressThread implements Runnable{

    /**
     * Initialises the lists to blank lists
     * @param hostGUI
     */
    public decompressThread(hostGui hostGUI)
    {
        files = new LinkedList<File>();
        configs = new LinkedList<runConfig>();
        gui = hostGUI;
    }

    /**
     * Adds a new file to the queue
     * @param run the run config, used to determine where to save the file
     * @param file the compressed file
     */
    public synchronized void add(runConfig run, File file)
    {
        files.add(file);
        configs.add(run);
        
    }

    public void run() {
        while(true)
        {
            while(files.size()==0) //while there are no more files to decompress
            {
                try {
                    gui.setDecompressMessage("");
                    Thread.sleep(10000);  //wait 10 seconds to see if new ones are added
                } catch (InterruptedException ex) {
                 
                }
            }
            gui.setDecompressMessage("Decompression in Progress");
            File current = files.pop();
            runConfig info = configs.pop();
            try {
                ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(current.toString())));
                if(zin.getNextEntry()!=null)
                {
                    byte buffer[] = new byte[1024];
                    int count;
                    System.out.println(info.getFileName());
                    BufferedOutputStream zout = new BufferedOutputStream(new FileOutputStream(info.getFileName(),true),1024);
                    while ((count = zin.read(buffer, 0, 1024)) != -1) {
                         zout.write(buffer, 0, count);
                    }
                    zout.flush();
                    zout.close();
                }
                zin.close();
                current.delete();
            } catch (IOException ex) {
                Logger.getLogger(decompressThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private LinkedList<File> files;
    private LinkedList<runConfig> configs;
    private hostGui gui;
}
