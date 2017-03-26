/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.model;

import com.js.pirategold.omdb.CachedOMDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author joris
 */
public class EigenValueSimilarMovieFinder implements ISimilarMovieFinder{
    
    private final Map<String, Set<String>> linkage = new HashMap<>();
    private final Map<String, Double> eigenvalues = new HashMap<>();
    private final List<String> recommendations = new ArrayList<>();
    
    private void setupLinkage(Collection<String> ids)
    {
        linkage.clear();
        for(String source : ids)
        {
            linkage.put(source, new HashSet<>());
            Movie mov = CachedOMDB.getMovie(source);
            for(String target : mov.getSimilar())
            {
                if(ids.contains(target))
                    linkage.get(source).add(target);
            }            
        }
    }
    
    private void calculateEigenvalues()
    {
        eigenvalues.clear();
        for(String id : linkage.keySet())
            eigenvalues.put(id, 1.0);
                
        double alpha = 0.85;
        for(int gen=0;gen<128;gen++)
        {
           Map<String,Double> ev2 = new HashMap<>();
           double pRand = (1.0 - alpha) / linkage.size();
           for(String id : linkage.keySet())
               ev2.put(id, pRand);
           
           for(String source : linkage.keySet())
           {
               double pLink = eigenvalues.get(source) / linkage.get(source).size();  
               for(String target : linkage.get(source))
               {
                   ev2.put(target, ev2.get(target) + (pLink * alpha));
               }
           }
           
           eigenvalues.clear();
           eigenvalues.putAll(ev2);
           ev2.clear();
        }                
    }

    private void vote(int k)
    {        
        Map<String, Double> votes = new HashMap<>();
        for(String id : eigenvalues.keySet())
        {
            Movie mov = CachedOMDB.getMovie(id);
            for(String rec : mov.getSimilar())
            if(!linkage.containsKey(rec))
            {
                if(votes.containsKey(rec))
                    votes.put(rec, votes.get(rec) + eigenvalues.get(id));
                else
                    votes.put(rec, eigenvalues.get(id));
            }
        }
        
        // sort
        List<Entry<String,Double>> entries = new ArrayList<>(votes.entrySet());
        java.util.Collections.sort(entries, new Comparator<Entry<String,Double>>(){
            @Override
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        
        // sublist
        recommendations.clear();
        for(int i=entries.size()-1;i>=0;i--)
        {
            recommendations.add(entries.get(i).getKey());
            if(recommendations.size() == k)
                break;
        }        
    }
    
    @Override
    public Collection<String> similar(Collection<String> ids) {
        setupLinkage(ids);
        calculateEigenvalues();
        vote(32);
        return recommendations;
    }
    
}
