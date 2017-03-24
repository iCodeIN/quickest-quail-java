/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.actions.server;

import com.js.pirategold.server.MyFlixServer;
import com.js.pirategold.ui.actions.AbstractIconAction;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author joris
 */
public class ToggleServerAction extends AbstractIconAction{

    private static String startTitle = java.util.ResourceBundle.getBundle("i18n/i18n").getString("server.start");
    private static String stopTitle = java.util.ResourceBundle.getBundle("i18n/i18n").getString("server.stop");
    
    public ToggleServerAction()
    {
        super(startTitle,"img/006-power.png");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(MyFlixServer.get().isRunning())
            stopServer();
        else
            startServer();
    }
    
    private void setIcon(String path)
    {                
        try {
            BufferedImage bi = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
            Icon ico = new ImageIcon(bi);
            super.putValue(Action.LARGE_ICON_KEY, ico);        
        } catch (IOException | NullPointerException ex) {
        }
    }    
    
    private void setText(String s)
    {
        super.putValue(Action.NAME, s);
    }
    
    private void startServer()
    {
        // start server        
        try {
            MyFlixServer.get().start();
        } catch (IOException ex) {
            Logger.getLogger(ToggleServerAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // set icon
        setIcon("img/007-stop.png");      
        
        // set text
        setText(stopTitle);
    }
    
    private void stopServer()
    {
        // stop server
        MyFlixServer.get().stop();
        
        // set icon
        setIcon("img/006-power.png");
        
        // set text
        setText(startTitle);
    }
    
}
