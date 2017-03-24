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
import java.util.Map.Entry;
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
public class CountryStat extends JPanel{
    
    public CountryStat()
    {
        setLayout(new BorderLayout());
        add(new ChartPanel(generateChart()), BorderLayout.CENTER);
    }
    
    private PieDataset generateDataset()
    {
        // gather data
        Map<String, Integer> countryFrequency = new HashMap<>();
        for(String id : DriveManager.get().getSelected().values())
        {
            Movie mov = CachedOMDB.getMovie(id);
            for(String genre : mov.getCountry())
            {
                if(!countryFrequency.containsKey(genre))
                {
                    countryFrequency.put(genre, 1);
                }else
                {
                    countryFrequency.put(genre, countryFrequency.get(genre) + 1);
                }
            }
        }
        
        // convert to proper format
        DefaultPieDataset dataset = new DefaultPieDataset( );
        for(Entry<String, Integer> en : countryFrequency.entrySet())
            dataset.setValue(en.getKey(), en.getValue());
        
      return dataset;              
    }
    
    private JFreeChart generateChart()
    {
        JFreeChart chart = ChartFactory.createPieChart(      
         "Nof movies per country",     // chart title 
         generateDataset(),         // data    
         true,                      // include legend   
         true, 
         false);
      return chart;        
    }
    
    
}
