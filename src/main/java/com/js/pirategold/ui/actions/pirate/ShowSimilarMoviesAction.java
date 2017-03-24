/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.actions.pirate;

import com.js.pirategold.model.Drive;
import com.js.pirategold.model.DriveManager;
import com.js.pirategold.model.Movie;
import com.js.pirategold.omdb.CachedOMDB;
import com.js.pirategold.ui.MovieTableModel;
import com.js.pirategold.ui.UI;
import com.js.pirategold.ui.actions.AbstractIconAction;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
        
        
        // naive algorithm for finding good recommendations
        Set<String> knownMovies = new HashSet<>();
        for(String imdbID : d.values())
            knownMovies.add(imdbID);
        Map<String,Integer> votes = new HashMap<>();
        for(String imdbID : d.values())
        {
            Movie mov = CachedOMDB.getMovie(imdbID);
            for(String similarImdbId : mov.getSimilar())
            {
                if(knownMovies.contains(similarImdbId))
                    continue;
                if(!votes.containsKey(similarImdbId))
                    votes.put(similarImdbId, 1);
                else
                    votes.put(similarImdbId, votes.get(similarImdbId) + 1);
            }
        }
        
        // sort entries
        List<Entry<String,Integer>> entries = new ArrayList<>(votes.entrySet());
        java.util.Collections.sort(entries, (Entry<String, Integer> o1, Entry<String, Integer> o2) -> o1.getValue().compareTo(o2.getValue()));
        
        // look them up
        List<Movie> movs = new ArrayList<>();
        for(int i=entries.size()-1;i>=java.lang.Math.max(0, entries.size() - 20);i--)
        {
            movs.add(CachedOMDB.getMovie(entries.get(i).getKey()));
        }
        
        JTable table = UI.get().getSimilarMoviesTable();
        table.setModel(new MovieTableModel(movs));
        
    }
        
}
