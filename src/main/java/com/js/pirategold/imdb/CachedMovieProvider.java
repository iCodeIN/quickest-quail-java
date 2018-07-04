/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.imdb;

import com.js.pirategold.model.Movie;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author joris
 */
public class CachedMovieProvider implements IMovieProvider{

    private static final Map<String, Movie> cache = new HashMap<>();

    private static final File rootDir = new File(System.getProperty("user.home"), "pirategold");
    private static final File sourceDir = new File(rootDir, "imdb");

    private IMovieProvider movieProvider = new TMDB();

    private CachedMovieProvider(){}

    private static CachedMovieProvider self = new CachedMovieProvider();

    public static CachedMovieProvider get(){ return self; }

    @Override
    public Movie getMovie(String query) { return null; }

    public Movie getMovieByID(String imdbID) {
        if (!sourceDir.exists()) {
            sourceDir.mkdirs();
        }

        if (cache.isEmpty()) {
            load();
        }

        if (cache.containsKey(imdbID)) {
            return cache.get(imdbID);
        }

        Movie mov = movieProvider.getMovieByID(imdbID);
        cache.put(imdbID, mov);

        storeMovie(mov);

        return mov;
    }

    public static void preload() {
        if(!sourceDir.exists())
            return;
        
        for (File f : sourceDir.listFiles()) {
            try {

                Scanner sc = new Scanner(f);
                String temp = "";
                while (sc.hasNextLine()) {
                    temp += sc.nextLine();
                }
                sc.close();

                Movie mov = new Movie();
                mov.putAll(new JSONObject(temp).toMap());
                cache.put(mov.getImdbID(), mov);

            } catch (FileNotFoundException | JSONException ex ) {
                Logger.getLogger(CachedMovieProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void load() {
        new Thread() {
            @Override
            public void run() {
                preload();
            }
        }.start();

    }

    private static void storeMovie(Movie mov) {
        if(mov.getImdbID().isEmpty())
            return;
        new Thread() {
            @Override
            public void run() {
                FileWriter writer = null;
                try {
                    File f = new File(sourceDir, mov.getImdbID());
                    writer = new FileWriter(f);
                    writer.write(new JSONObject(mov).toString(3));
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(CachedMovieProvider.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        Logger.getLogger(CachedMovieProvider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
}
