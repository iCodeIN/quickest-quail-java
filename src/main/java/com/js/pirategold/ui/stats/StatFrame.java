/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.stats;

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
        tabs.add("Genre", new GenreStat());
        tabs.add("Country", new CountryStat());
        tabs.add("Language", new LanguageStat());
        tabs.add("Year", new YearStat());
        tabs.add("Rating", new RatingStat());
        
        add(tabs, BorderLayout.CENTER);
        pack();
        
        // set position
        int width = getWidth();
        int height = getHeight();
        
        int centerX = parent.getX() + parent.getWidth()/2;
        int centerY = parent.getY() + parent.getHeight()/2;
        
        setBounds(centerX - width/2, centerY - height/2, width, height);        
        
    }
}
