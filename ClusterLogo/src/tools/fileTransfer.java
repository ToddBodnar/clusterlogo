/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JProgressBar;

/**
 * A class with static functions to send and receive a file over the internet
 *
 * @author Todd Bodnar
 */
public class fileTransfer {

    /**
     * Receives a file over the internet
     *
     * @param output The location to save the file
     * @param inStream The stream from the connection
     * @param progressBar A progress bar to track downloading, can be set as null if unwanted
     * @throws IOException
     */
    public static void getFile(File output, ObjectInputStream inStream, JProgressBar progressBar) throws IOException
    {
        FileOutputStream out = new FileOutputStream(output,true);

        if(progressBar == null)
            progressBar = new JProgressBar();

        progressBar.setValue(0);


        long length = inStream.readLong();

        progressBar.setString(length+"");

        for(long ct=0;ct<length;ct++)
        {
            out.write(inStream.readByte());

            if(ct%(length/100+1)==0)
                progressBar.setValue((int) (100 * ct / length));
        }

        progressBar.setValue(100);
        progressBar.setString("");
        out.close();
    }


    /**
     * Sends a file over the internet
     *
     * @param input The file to be sent
     * @param outStream The stream to the computer that is to receive the file
     * @param progressBar A JProgressBar that keeps track of the upload progress. Can be null if unwanted
     * @throws IOException
     */
    public static void sendFile(File input, ObjectOutputStream outStream, JProgressBar progressBar) throws IOException
    {
        FileInputStream in = new FileInputStream(input);

        if(progressBar == null)
            progressBar = new JProgressBar();

        progressBar.setValue(0);

        long length = input.length();

        outStream.writeLong(length);

        for (long ct = 0; ct < length; ct++) {
            int inputs = in.read();

            outStream.writeByte(inputs);

            if(ct%(length/100+1)==0)
                progressBar.setValue((int) (100 * ct / length));
        }
        outStream.flush();

        progressBar.setValue(100);

        in.close();
    }
}
