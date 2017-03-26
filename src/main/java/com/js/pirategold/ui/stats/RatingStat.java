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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author joris
 */
public class RatingStat extends JPanel{
    
    public RatingStat()
    {
        setLayout(new BorderLayout());
        add(new ChartPanel(generateChart()), BorderLayout.CENTER);
    }    
    
    private CategoryDataset generateDataset() {
        Map<Double, Integer> ratingFrequency = new HashMap<>();
        for (String id : DriveManager.get().getSelected().values()) {
            Movie mov = CachedOMDB.getMovie(id);
            
            int temp = ((int) (mov.getImdbRating() * 10));
            double rating = (temp - temp % 5) / 10.0;
            
            if (!ratingFrequency.containsKey(rating)) {
                ratingFrequency.put(rating, 1);
            } else {
                ratingFrequency.put(rating, ratingFrequency.get(rating) + 1);
            }

        }

        // sort
        List<Entry<Double,Integer>> entries = new ArrayList<>(ratingFrequency.entrySet());
        java.util.Collections.sort(entries, new Comparator<Entry<Double,Integer>>() {
            @Override
            public int compare(Entry<Double, Integer> o1, Entry<Double, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        
        // convert to proper format
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();        
        for (Map.Entry<Double, Integer> en : entries) {
            dataset.addValue(en.getValue(), "nof movies", en.getKey());
        }

        // return
        return dataset;
    }
    
    private JFreeChart generateChart()
    {
         JFreeChart lineChart = ChartFactory.createLineChart(
            "Nof movies per rating",
            "Rating",
            "Number of Movies",
            generateDataset(),
            PlotOrientation.VERTICAL,
            true,
            true,
            false);
         return lineChart;        
    }   
}
