/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.omdb;

import com.js.pirategold.model.Movie;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author joris
 */
public class OMDB {
    
    public static Movie getMovie(String query)
    {
        try {
            return _getMovie(query);
        } catch (IOException ex) {
            Logger.getLogger(OMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Movie();        
    }
    
    private static Movie _getMovie(String query) throws MalformedURLException, IOException
    {
        // if there is a year in the query, separate it
        int yearIndex = -1;
        String[] parts = query.split(" ");
        for(int i=0;i<parts.length;i++)
        {
            try{
                int year = Integer.parseInt(parts[i]);
                if(year >= 1800 && year <= 3000)
                {
                    yearIndex = i;
                }
            }catch(Exception ex){}
        }
        if(yearIndex != -1)
        {
            query = "";
            for(int i=0;i<parts.length;i++)
            {
                if(i != yearIndex)
                    query += " " + parts[i];
            }
        }
        query = query.trim().replaceAll(" +", " ");
        
        String urlString = "";
        if(yearIndex == -1)
            urlString = "http://www.omdbapi.com/?t=" + query.replaceAll(" ", "+") + "&plot=full";
        else
            urlString = "http://www.omdbapi.com/?t=" + query.replaceAll(" ", "+") + "&y="+parts[yearIndex] + "&plot=full";
        
        Scanner sc = new Scanner(new URL(urlString).openStream());
        String body = "";
        while(sc.hasNextLine())
        {
            body += sc.nextLine();
        }
        sc.close();
        
        Movie retval = new Movie();
        retval.putAll(new JSONObject(body).toMap());
        return retval;
    }
    
    public static Movie getMovieByID(String imdbID)
    {        
        try {
            return _getMovieByID(imdbID);
        } catch (IOException ex) {
            Logger.getLogger(OMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Movie();
    }
    
    private static Movie _getMovieByID(String imdbID) throws MalformedURLException, IOException
    {
        String urlString = "http://www.omdbapi.com/?i=" + imdbID + "&plot=full";
        
        Scanner sc = new Scanner(new URL(urlString).openStream());
        String body = "";
        while(sc.hasNextLine())
        {
            body += sc.nextLine();
        }
        sc.close();
        
        Movie retval = new Movie();
        retval.putAll(new JSONObject(body).toMap());
        
        // add IMDB data (similar movies)
        retval.put("similar", java.util.Arrays.asList(IMDB.getSimilarMovies(imdbID)));
        
        return retval;
    }
}
