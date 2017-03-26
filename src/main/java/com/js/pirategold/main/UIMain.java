/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.main;

import com.js.pirategold.model.DriveManager;
import com.js.pirategold.omdb.CachedOMDB;
import com.js.pirategold.ui.UI;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.jdesktop.swingx.JXFrame;

/**
 *
 * @author joris
 */
public class UIMain {
    
    public static void main(String[] args)
    {
        // load cache
        CachedOMDB.preload();
        
        // set look and feel
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        
        // build UI
        JXFrame frame = UI.get();
   
        // set icon
        try {
            BufferedImage bi = ImageIO.read(UIMain.class.getClassLoader().getResourceAsStream("img/008-poison.png"));
            frame.setIconImage(bi);
        } catch (NullPointerException | IOException ex) {
        }
       
        
        // if there is only 1 drive, select it
        if(DriveManager.get().all().size() == 1)
        {
            DriveManager.get().setSelected(DriveManager.get().all().iterator().next());
        }
        
        // set size
        Dimension ss = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(50, 50, (int) ss.getWidth() - 100, (int) ss.getHeight() - 100);
                
        // display
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
