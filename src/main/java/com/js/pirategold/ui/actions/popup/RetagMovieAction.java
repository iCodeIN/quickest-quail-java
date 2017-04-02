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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
}
