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
import java.io.File;
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
public class ExtensionStat extends JPanel {

    public ExtensionStat() {
        setLayout(new BorderLayout());
        add(new ChartPanel(generateChart()), BorderLayout.CENTER);
    }

    private PieDataset generateDataset() {
        // gather data
        Map<Object, Number> extensionFrequency = new HashMap<>();
        for (File f : DriveManager.get().getSelected().keySet()) {
            String extension = f.getName();
            extension = extension.substring(extension.lastIndexOf(".")).toUpperCase();
            
            if (!extensionFrequency.containsKey(extension)) {
                extensionFrequency.put(extension, 1);
            } else {
                extensionFrequency.put(extension, extensionFrequency.get(extension).doubleValue() + 1);
            }

        }

        // trim
        MapTrimmer.trim(extensionFrequency, 0.01);

        // convert to proper format
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Entry<Object, Number> en : extensionFrequency.entrySet()) {
            dataset.setValue(en.getKey().toString(), en.getValue());
        }

        return dataset;
    }

    private JFreeChart generateChart() {
        JFreeChart chart = ChartFactory.createPieChart(
                "Nof movies per format", // chart title 
                generateDataset(), // data    
                true, // include legend   
                true,
                false);
        return chart;
    }

}
