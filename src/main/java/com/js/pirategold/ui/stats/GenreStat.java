/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.stats;

import com.js.pirategold.model.DriveManager;
import com.js.pirategold.model.Movie;
import com.js.pirategold.omdb.CachedOMDB;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author joris
 */
public class GenreStat extends JPanel{
    
    public GenreStat()
    {
        setLayout(new BorderLayout());
        add(new ChartPanel(generateChart()), BorderLayout.CENTER);
    }
    
    private PieDataset generateDataset()
    {
        // gather data
        Map<Object, Number> genreFrequency = new HashMap<>();
        for(String id : DriveManager.get().getSelected().values())
        {
            Movie mov = CachedOMDB.getMovie(id);
            for(String genre : mov.getGenre())
            {
                if(!genreFrequency.containsKey(genre))
                {
                    genreFrequency.put(genre, 1);
                }else
                {
                    genreFrequency.put(genre, genreFrequency.get(genre).doubleValue() + 1);
                }
            }
        }
        
        // trim
        MapTrimmer.trim(genreFrequency, 0.01);
        
        // convert to proper format
        DefaultPieDataset dataset = new DefaultPieDataset( );
        for(Map.Entry<Object, Number> en : genreFrequency.entrySet())
            dataset.setValue(en.getKey().toString(), en.getValue());
        
      return dataset;              
    }
    
    private JFreeChart generateChart()
    {
        JFreeChart chart = ChartFactory.createPieChart(      
         java.util.ResourceBundle.getBundle("i18n/i18n").getString("stat.genre.title"),     // chart title 
         generateDataset(),         // data    
         true,                      // include legend   
         true, 
         false);
      return chart;        
    }    
}
