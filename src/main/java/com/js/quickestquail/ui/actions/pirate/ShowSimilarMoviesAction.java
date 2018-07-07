/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.ui.actions.pirate;

import com.js.quickestquail.model.Drive;
import com.js.quickestquail.model.DriveManager;
import com.js.quickestquail.model.EigenValueSimilarMovieFinder;
import com.js.quickestquail.model.ISimilarMovieFinder;
import com.js.quickestquail.model.Movie;
import com.js.quickestquail.imdb.CachedMovieProvider;
import com.js.quickestquail.ui.MovieTableModel;
import com.js.quickestquail.ui.UI;
import com.js.quickestquail.ui.actions.AbstractIconAction;
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
