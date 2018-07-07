/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.stats;

import com.js.pirategold.model.DriveManager;
import com.js.pirategold.model.Movie;
import com.js.pirategold.imdb.CachedMovieProvider;

import java.awt.*;
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
public class LanguageStat extends JPanel{

    public LanguageStat() {
        setLayout(new BorderLayout());
        add(new ChartPanel(generateChart()), BorderLayout.CENTER);
    }

    private PieDataset generateDataset() {
        // gather data
        Map<Object, Number> languageFrequency = new HashMap<>();
        for (String id : DriveManager.get().getSelected().values()) {
            Movie mov = CachedMovieProvider.get().getMovieByID(id);
            if(mov.getLanguage() == null)
                continue;
            for (String lang : mov.getLanguage()) {
                if (!languageFrequency.containsKey(lang)) {
                    languageFrequency.put(lang, 1);
                } else {
                    languageFrequency.put(lang, languageFrequency.get(lang).doubleValue() + 1);
                }
            }
        }
        
        // trim
        MapTrimmer.trim(languageFrequency, 0.01);

        // convert to proper format
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<Object, Number> en : languageFrequency.entrySet()) {
            dataset.setValue(en.getKey().toString(), en.getValue());
        }

        return dataset;
    }

    private JFreeChart generateChart() {
        JFreeChart chart = ChartFactory.createPieChart(
                java.util.ResourceBundle.getBundle("i18n/i18n").getString("stat.language.title"), // chart title 
                generateDataset(), // data    
                true, // include legend   
                true,
                false);
        chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        return chart;
    }
}
