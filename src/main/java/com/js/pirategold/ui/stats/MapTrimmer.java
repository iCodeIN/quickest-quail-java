/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.stats;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author joris
 */
public class MapTrimmer {
        
   
    public static void trim(Map<Object, Number> input, double minPercentile)
    {
        double totalMass = 0;
        for(Number v : input.values())
            totalMass += v.doubleValue();
        
        Set<Object> keysToRemove = new HashSet<>();
        for(Entry<Object,Number> en : input.entrySet())
        {
            double  p = en.getValue().doubleValue() / totalMass;
            if(p < minPercentile)
            {
                keysToRemove.add(en.getKey());
            }
        }
        
        for(Object key : keysToRemove)
            input.remove(key);
    }
}
