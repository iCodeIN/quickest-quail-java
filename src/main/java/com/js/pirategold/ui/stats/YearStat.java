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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author joris
 */
public class YearStat extends JPanel{

    public YearStat()
    {
        setLayout(new BorderLayout());
        add(new ChartPanel(generateChart()), BorderLayout.CENTER);
    }    
    
    private CategoryDataset generateDataset() {
        Map<Integer, Integer> yearFrequency = new HashMap<>();
        for (String id : DriveManager.get().getSelected().values()) {
            Movie mov = CachedOMDB.getMovie(id);
            int year = mov.getYear();

            if (!yearFrequency.containsKey(year)) {
                yearFrequency.put(year, 1);
            } else {
                yearFrequency.put(year, yearFrequency.get(year) + 1);
            }

        }

        // convert to proper format
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Entry<Integer, Integer> en : yearFrequency.entrySet()) {
            dataset.addValue(en.getValue(), "nof movies", en.getKey());
        }

        // return
        return dataset;
    }
    
    private JFreeChart generateChart()
    {
         JFreeChart lineChart = ChartFactory.createLineChart(
            "Nof movies per year",
            "Years",
            "Number of Movies",
            generateDataset(),
            PlotOrientation.VERTICAL,
            true,
            true,
            false);
         return lineChart;        
    }
}
