/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.io;

import com.js.quickestquail.imdb.IMovieProvider;
import com.js.quickestquail.imdb.TMDB;
import com.js.quickestquail.model.Movie;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author joris
 */
public class RegexFileHandler implements IFileHandler{

    private String[][] regexes = {{"(.*)\\(([0123456789]+)\\)", "{1} {2}"},       // remove year
                                  {"(.*)\\[([0123456789]+)\\]", "{1} {2}"},       // remove year                                    
                                  {"(.*)\\{([0123456789]+)\\}", "{1} {2}"},       // remove year    */
                                
                                  {"(.*)S[0123456789]+E[0123456789]+(.*)","{1} {2}"},    // remove S1E01
                                  
                                  {"(.*)1080[pP](.*)","{1} {2}"},           // remove quality information
                                  {"(.*)720[pP](.*)","{1} {2}"},
                                  {"(.*)(b|B)(l|L)(u|U)(r|R)(a|A)(y|Y)(.*)","{1} {8}"},
                                  {"(.*)(d|D)(v|V)(d|D)(r|R)(i|I)(p|P)(.*)","{1} {8}"},
                                  {"(.*)(h|H)(d|D)-*(t|T)(s|S)(.*)","{1} {6}"},                                  
                                                                    
                                  {"(.*)[xX]264(.*)","{1} {2}"},            // remove codec information
                                  {"(.*)[hH]264(.*)","{1} {2}"},
                                  {"(.*)[xX]265(.*)","{1} {2}"},
                                  {"(.*)[hH]265(.*)","{1} {2}"},
                                  
                                  {"(.*)\\.(.*)","{1} {2}"},                // remove .
                                  {"(.*)  (.*)","{1} {2}"}                  // remove <space><space>
                                 };

    private IMovieProvider movieProvider = new TMDB();

    @Override
    public Movie process(File f) {
        
        String name = f.getName();
        name = name.substring(0, name.lastIndexOf("."));

        System.out.println("processing " + f);
        
        boolean modified = true;
        while(modified)
        {
            String prevName = name;
            modified = false;
            for(int i=0;i<regexes.length;i++)
            {
                Pattern pat = Pattern.compile(regexes[i][0]);
                Matcher mat = pat.matcher(name);
                if(mat.matches())
                {
                    name = regexes[i][1];
                    for(int j=0;j<=mat.groupCount();j++)
                    {
                        name = name.replaceAll("\\{" + j + "\\}", mat.group(j));
                    }
                    modified = !prevName.equals(name);
                    break;
                }
            }
        }
        
        String[] parts = name.split(" ");
        for(int i = parts.length;i > 0;i--)
        {
            String partialName = "";
            for(int j=0;j<i;j++)
            {
                partialName += " " + parts[j];
            }
            Movie mov = movieProvider.getMovie(partialName);
            if(!mov.getImdbID().isEmpty())
                return mov;
        }
        
        return new Movie();
    }
    
    
}
