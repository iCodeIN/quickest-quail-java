/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.ui.stats;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author joris
 */
public class StatFrame extends JFrame{
  
    public StatFrame(JFrame parent)
    {
        setLayout(new BorderLayout());
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.add(java.util.ResourceBundle.getBundle("i18n/i18n").getString("stat.genre"), new GenreStat());
        tabs.add(java.util.ResourceBundle.getBundle("i18n/i18n").getString("stat.country"), new CountryStat());
        tabs.add(java.util.ResourceBundle.getBundle("i18n/i18n").getString("stat.language"), new LanguageStat());
        tabs.add(java.util.ResourceBundle.getBundle("i18n/i18n").getString("stat.year"), new YearStat());
        tabs.add(java.util.ResourceBundle.getBundle("i18n/i18n").getString("stat.rating"), new RatingStat());
        tabs.add(java.util.ResourceBundle.getBundle("i18n/i18n").getString("stat.extension"), new ExtensionStat());
        
        add(tabs, BorderLayout.CENTER);
        pack();
        
        // set position
        int width = getWidth();
        int height = getHeight();        
        int centerX = parent.getX() + parent.getWidth()/2;
        int centerY = parent.getY() + parent.getHeight()/2;        
        setBounds(centerX - width/2, centerY - height/2, width, height);        
        
        // copy icon
        this.setIconImage(parent.getIconImage());
    }
}
