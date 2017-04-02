/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.actions.popup;

import com.js.pirategold.model.Movie;
import com.js.pirategold.ui.MovieTableModel;
import com.js.pirategold.ui.actions.AbstractIconAction;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

/**
 *
 * @author joris
 */
public class PlayMovieAction extends AbstractIconAction{

    private JTable table;
    
    public PlayMovieAction(JTable table) {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("popup.play"), "img/015-play.png");
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        Movie mov = ((MovieTableModel) table.getModel()).getMovieAt(row);
        
        File f = mov.containsKey("file") ? (File) mov.get("file") : null;
        if(f == null || !f.exists())
            return;
        
        String command = java.util.ResourceBundle.getBundle("settings/settings").getString("local.play.command");
        command = command.replace("{0}", f.getAbsolutePath());
        
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
            Logger.getLogger(PlayMovieAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
}
