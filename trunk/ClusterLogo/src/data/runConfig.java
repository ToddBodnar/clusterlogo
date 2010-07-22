/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

import java.io.File;
import java.io.Serializable;
import java.util.Vector;

/**
 * Configuration for one run of an experiment
 * Sent from the server to the client to tell it how to set up netlogo
 *
 * @author Todd Bodnar
 * @version 0.2
 */
public class runConfig implements Serializable {

    /**
     * The number of times the "go" button is pressed
     */
    private long length;

    /**
     * The name of the file that the file will be eventually saved as
     */
    private String fileName;

    /**
     * A vector that holds all of the set commands that will be called
     */
    private Vector<String> setCommands;

    /**
     * File of the nLogo project to run the experiment on
     */
    private File nLogo;


    public String toString()
    {
        String output = fileName + " ";
        for(int ct=0;ct<setCommands.size();ct++)
            output+= setCommands.get(ct)+" ";
        output += "For " + length + " runs using "+nLogo.toString();

        return output;
    }

    /**
     * Constructs a runConfig
     * @param resultFile the name of the file to be returned to the server
     * @param ticks how many times the go button is pressed
     * @param commands a vector of all of the set commands called before setup is pressed
     */
    public runConfig(File nLogo, String resultFile, long ticks, Vector<String> commands)
    {
        this.nLogo = nLogo;
        fileName = resultFile;
        length = ticks;
        setCommands = commands;
    }

    /**
     * Gets the location and name of the file that the data will be saved as
     * @return the fileName
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * Gets the number of times "go" is pressed by the client
     * @return the number of times the client should press the "go" button
     */
    public long getLength()
    {
        return length;
    }

    /**
     *
     * @return a string of all of the set commands that should be applied to the simulation
     */
    public Vector<String> getCommands()
    {
        return setCommands;
    }

    /**
     *
     * @return A File pointing to the *.nLogo file to be run
     */
    public File getNLogo()
    {
        return nLogo;
    }

    /**
     * Determines if this is equal to another object.
     * Calculated by comparing class, fileName, length, and each of the setCommands (in order)
     * @param otherObj the other object to test
     * @return if the two objects are the same
     */
    @Override
    public boolean equals(Object otherObj)
    {
        if(!(otherObj instanceof runConfig))return false;
        
        runConfig other = (runConfig)otherObj;

        if(!fileName.equals(other.fileName))
            return false;
        if(length!=other.length)
            return false;
        if(setCommands.size()!=other.setCommands.size())
            return false;
        
        for(int ct=0;ct<setCommands.size();ct++)
        {
            if(!setCommands.get(ct).equals(other.setCommands.get(ct)))
                return false;
        }
        return true;
        
    }
}
