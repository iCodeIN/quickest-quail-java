/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.model;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author joris
 */
public class Movie extends HashMap<String, Object> {
    
    public String getPoster()
    {
        return getAsString("Poster");
    }
    
    public String[] getGenre()
    {
        return getAsStringArray("Genre");
    }
    
    public String[] getWriter()
    {
        return getAsStringArray("Writer");        
    }
    
    public String[] getDirector()
    {
        return getAsStringArray("Director");        
    }
    
    public String[] getActors()
    {
        return getAsStringArray("Actors");        
    }
    
    public String[] getCountry()
    {
        return getAsStringArray("Country");        
    }    
    
    public String[] getLanguage()
    {
        return getAsStringArray("Language");        
    }
    
    public String getPlot()
    {
        return getAsString("Plot");
    }
    
    public String getTitle()
    {
        return getAsString("Title");
    }
    
    public int getYear()
    {
        return getAsInteger("Year");
    }
    
    public double getImdbRating()
    {
        return getAsDouble("imdbRating");
    }
    
    public int getImdbVotes()
    {
        return getAsInteger("imdbVotes");
    }
    
    public String getImdbID()
    {
        return getAsString("imdbID");
    }
    
    public double getMetaScore()
    {
        return getAsDouble("Metascore");
    };
    
    public Collection<String> getSimilar()
    {
        return (Collection<String>) get("similar");
    }
    
    private double getAsDouble(String propertyName)
    {
        try
        {
            return containsKey(propertyName) ? Double.parseDouble(get(propertyName).toString()) : -1;
        }
        catch(Exception ex)
        {
            return -1;
        }          
    }
    
    private int getAsInteger(String propertyName)
    {
        try
        {
            return containsKey(propertyName) ? Integer.parseInt(get(propertyName).toString().replaceAll("[.,]", "")) : -1;
        }
        catch(Exception ex)
        {
            return -1;
        }           
    }
    
    private String getAsString(String propertyName)
    {
        return containsKey(propertyName) ? get(propertyName).toString() : "";
    }
    
    private String[] getAsStringArray(String propertyName)
    {
        String[] parts = getAsString(propertyName).split(", ");
        java.util.Arrays.sort(parts);
        return parts;
    }
}
