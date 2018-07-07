/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.ui.actions.popup;

import com.js.quickestquail.model.Drive;
import com.js.quickestquail.model.Drive.MovieRemovedEvent;
import com.js.quickestquail.model.DriveManager;
import com.js.quickestquail.model.Movie;
import com.js.quickestquail.ui.MovieTableModel;
import com.js.quickestquail.ui.UI;
import com.js.quickestquail.ui.actions.AbstractIconAction;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author joris
 */
public class DeleteMovieAction extends AbstractIconAction{
  
    private JTable table;
    
    public DeleteMovieAction(JTable table) {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("popup.delete"), "img/013-close.png");
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        Movie mov = ((MovieTableModel) table.getModel()).getMovieAt(row);
        
        // dialog
        File toRemove = (File) mov.get("file");
        String fileName = mov.containsKey("file") ? toRemove.getName() : "";        
        int opt = JOptionPane.showConfirmDialog(
                UI.get(), 
                "Are you sure you want to delete this movie?\nTitle : " + mov.getTitle() + "\nFile : " + fileName + "", 
                java.util.ResourceBundle.getBundle("i18n/i18n").getString("popup.delete"),
                JOptionPane.YES_NO_CANCEL_OPTION);
        
        if(opt != JOptionPane.YES_OPTION)
            return;
               
        System.out.println(toRemove);
        
        // remove       
        Drive d = DriveManager.get().getSelected();
        d.remove(toRemove);        
        d.actionPerformed(new MovieRemovedEvent(mov));
        DriveManager.get().printDebug();
    }
    
}
