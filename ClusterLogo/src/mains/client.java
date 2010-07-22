/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mains;

import gui.clientGuiPerThread;
import gui.clientGuiRunner;
import java.awt.Container;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import threads.clientThread;

/**
 * The entry point for the client
 * @author Todd Bodnar
 */
public class client {
    public static void main(String[] args)
    {
        String hostIp = JOptionPane.showInputDialog("Enter host ip");
        int threads = Integer.parseInt(JOptionPane.showInputDialog("Number of threads"));

        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.showSaveDialog(null);

        JFrame frame = new JFrame("Cluster Logo Client");
        Container c = frame.getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

        clientGuiRunner runner = new clientGuiRunner();

        c.add(runner);
        for(int ct=0;ct<threads;ct++)
        {
            clientGuiPerThread threadGui = new clientGuiPerThread();

            clientThread thread = new clientThread(threadGui,runner,new File(jfc.getSelectedFile().getAbsolutePath()+File.separatorChar+ct+File.separatorChar),hostIp);
  
            c.add(new JSeparator(JSeparator.HORIZONTAL));
            c.add(threadGui);

            Thread t = new Thread(thread);
            t.start();
        }

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(540, 400);
    }
}
