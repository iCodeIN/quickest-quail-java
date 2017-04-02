/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.model.event;

/**
 *
 * @author joris
 */
public interface IActionModel {
    
    public void addListener(IActionModelListener listener);
    
    public void addListener(IActionModelListener listener, Class eventType);
    
    public void removeListener(IActionModelListener listener);
    
    public void removeListener(IActionModelListener listener, Class eventType);
    
    public void actionPerformed(IActionEvent event);
    
}
