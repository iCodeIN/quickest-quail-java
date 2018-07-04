/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.actions.pirate;

import com.js.pirategold.model.Drive;
import com.js.pirategold.model.DriveManager;
import com.js.pirategold.model.EigenValueSimilarMovieFinder;
import com.js.pirategold.model.ISimilarMovieFinder;
import com.js.pirategold.model.Movie;
import com.js.pirategold.imdb.CachedMovieProvider;
import com.js.pirategold.ui.MovieTableModel;
import com.js.pirategold.ui.UI;
import com.js.pirategold.ui.actions.AbstractIconAction;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

/**
 *
 * @author joris
 */
public class ShowSimilarMoviesAction extends AbstractIconAction{
    
    public ShowSimilarMoviesAction()
    {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("pirate.showsimilar"),"img/009-treasure.png");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        Drive d = DriveManager.get().getSelected();
        if (d == null) {
            return;
        }

        if (d.isEmpty()) {
            return;
        }
       
        
        // Eigenvalue implementation of ISimilarMovieFinder
        ISimilarMovieFinder finder = new EigenValueSimilarMovieFinder();
       
        // look them up
        List<Movie> movs = new ArrayList<>();
        for(String mov : finder.similar(d.values()))
        {
            movs.add(CachedMovieProvider.get().getMovieByID(mov));
        }
        
        // set TableModel
        JTable table = UI.get().getSimilarMoviesTable();
        table.setModel(new MovieTableModel(movs));
        
    }
        
}
