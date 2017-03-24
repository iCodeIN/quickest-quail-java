/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.omdb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author joris
 */
public class IMDB {
    
    public static String[] getSimilarMovies(String imdbId)
    {
        try{
            return _getSimilarMovies(imdbId);
        }catch(Exception ex){
            return new String[]{};
        }
    }
    
    private static String[] _getSimilarMovies(String imdbId) throws MalformedURLException, IOException
    {
        String path = "http://www.imdb.com/title/" + imdbId;
        
        String content = "";
        Scanner sc = new Scanner(new URL(path).openStream());
        while(sc.hasNextLine())
        {
            content += sc.nextLine();
        }
        sc.close();
        
        Document doc = Jsoup.parse(content);
        Elements titleRecs = doc.select("div#titleRecs");
        Element titleRecElement = titleRecs.isEmpty() ? null : titleRecs.get(0);
        
        if(titleRecElement == null)
            return new String[]{};
        
        Set<String> similarMovieIds = new HashSet<>();
        Pattern pat = Pattern.compile("/title/(tt[0123456789]{7})/");
        Elements links = titleRecElement.select("a[href]");
        for(Element link : links)
        {
            String linkTarget = link.attr("href");
            Matcher mat = pat.matcher(linkTarget);
            if(mat.find())
            {
                similarMovieIds.add(mat.group(1));
            }
        }        
        
        return similarMovieIds.toArray(new String[similarMovieIds.size()]);
    }
}
