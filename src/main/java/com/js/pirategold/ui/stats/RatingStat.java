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
            double rating = mov.getImdbRating();

            if (!ratingFrequency.containsKey(rating)) {
                ratingFrequency.put(rating, 1);
            } else {
                ratingFrequency.put(rating, ratingFrequency.get(rating) + 1);
            }

        }

        // convert to proper format
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<Double, Integer> en : ratingFrequency.entrySet()) {
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
