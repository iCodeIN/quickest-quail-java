/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.ui.actions.popup;

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
public class RetagMovieAction extends AbstractIconAction {
     
    private JTable table;
    
    public RetagMovieAction(JTable table) {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("popup.retag"), "img/014-tag.png");
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
                "Are you sure you want to re-tag this movie?\nTitle : " + mov.getTitle() + "\nFile : " + fileName + "", 
                java.util.ResourceBundle.getBundle("i18n/i18n").getString("popup.delete"),
                JOptionPane.YES_NO_CANCEL_OPTION);
        
        if(opt != JOptionPane.YES_OPTION)
            return;
        
        
        JOptionPane.showInputDialog(table, 
                "Enter the new IMDB-ID for this file:", 
                java.util.ResourceBundle.getBundle("i18n/i18n").getString("popup.retag"), 
                JOptionPane.INFORMATION_MESSAGE, 
                null, 
                null, 
                mov.getImdbID());
        
    }   
}
