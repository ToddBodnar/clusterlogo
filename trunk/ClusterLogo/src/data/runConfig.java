package data;

import java.io.File;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Configuration for one run of an experiment
 * Sent from the server to the client to tell it how to set up netlogo
 *
 * @author Todd Bodnar
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
    private String nLogo;

    /**
     * The time when this was assigned
     */
    private long startTime;


    public String toString()
    {
        String output = fileName + ";";
        for(int ct=0;ct<setCommands.size();ct++)
            output+= setCommands.get(ct)+" ";
        output += ";For;" + length + ";runs using;"+nLogo;

        return output;
    }

    public runConfig(String input) throws Exception
    {
        StringTokenizer mainParts = new StringTokenizer(input, ";");
        fileName = mainParts.nextToken();
        StringTokenizer commands = new StringTokenizer(mainParts.nextToken(), " ");
        setCommands = new Vector<String>();

        while (commands.hasMoreElements()) {
            
            setCommands.add(commands.nextToken() + " " + commands.nextToken());
        }

        mainParts.nextToken();

        length = Long.parseLong(mainParts.nextToken());

        mainParts.nextToken();

        nLogo = mainParts.nextToken();
    }

    /**
     * Sets startTime to the current time
     */
    public void setStarted()
    {
        startTime = System.currentTimeMillis();
    }

    /**
     * Determines how long, in milliseconds, it has been since this was started
     * @return The time since this started
     */
    public long timeSinceStart()
    {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Reports if the task has been timed out
     * @param hours the hours before an element times out
     * @return true if this is timed out, false otherwise
     */
    public boolean isTimedOut(float hours)
    {
        return hours*60*60*1000 <= timeSinceStart();
    }

    /**
     * Constructs a runConfig
     * @param resultFile the name of the file to be returned to the server
     * @param ticks how many times the go button is pressed
     * @param commands a vector of all of the set commands called before setup is pressed
     */
    public runConfig(File nLogo, String resultFile, long ticks, Vector<String> commands)
    {
        this.nLogo = nLogo.toString();
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
        return new File(nLogo);
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
