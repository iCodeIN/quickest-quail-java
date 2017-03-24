/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joris
 */
public class ActionModel {
    
    private List<ActionListener> listeners = new ArrayList<>();
    
    public void addActionListener(ActionListener l)
    {
        listeners.add(l);
    }
    
    public void removeActionListener(ActionListener l)
    {
        listeners.remove(l);
    }
    
    public void actionPerformed()
    {
        ActionEvent evt = new ActionEvent(this, this.hashCode(), "");        
        for(int i=listeners.size()-1;i>=0;i--)
        {
            listeners.get(i).actionPerformed(evt);
        }
    }
    
}
