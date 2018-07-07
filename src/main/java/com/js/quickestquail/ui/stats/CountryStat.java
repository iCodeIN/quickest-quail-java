/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.ui.stats;

import com.js.quickestquail.model.DriveManager;
import com.js.quickestquail.model.Movie;
import com.js.quickestquail.imdb.CachedMovieProvider;

import java.awt.*;
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
        Map<Object, Number> countryFrequency = new HashMap<>();
        for(String id : DriveManager.get().getSelected().values())
        {
            Movie mov = CachedMovieProvider.get().getMovieByID(id);
            if(mov.getCountry() == null)
                continue;
            for(String genre : mov.getCountry())
            {
                if(!countryFrequency.containsKey(genre))
                {
                    countryFrequency.put(genre, 1);
                }else
                {
                    countryFrequency.put(genre, countryFrequency.get(genre).doubleValue() + 1);
                }
            }
        }
   
        // trim
        MapTrimmer.trim(countryFrequency, 0.01);
        
        // convert to proper format
        DefaultPieDataset dataset = new DefaultPieDataset( );
        for(Entry<Object, Number> en : countryFrequency.entrySet())
            dataset.setValue(en.getKey().toString(), en.getValue());
        
      return dataset;              
    }
    
    private JFreeChart generateChart()
    {
        JFreeChart chart = ChartFactory.createPieChart(      
         java.util.ResourceBundle.getBundle("i18n/i18n").getString("stat.country.title"),     // chart title 
         generateDataset(),         // data    
         true,                      // include legend   
         true, 
         false);
        chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
        chart.getPlot().setBackgroundPaint(Color.WHITE);
      return chart;        
    }
    
    
}
