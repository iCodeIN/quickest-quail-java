/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.ui.stats;

import com.js.quickestquail.model.DriveManager;

import java.awt.*;
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
                java.util.ResourceBundle.getBundle("i18n/i18n").getString("stat.extension.title"), // chart title 
                generateDataset(), // data    
                true, // include legend   
                true,
                false);
        chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        return chart;
    }

}
