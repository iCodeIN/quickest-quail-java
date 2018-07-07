/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.server;

import com.js.quickestquail.model.DriveManager;
import com.js.quickestquail.model.Movie;
import com.js.quickestquail.imdb.CachedMovieProvider;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author joris
 */
public class ViewAllMoviesHandler implements HttpHandler{

      private final File rootDir = new File(new File(System.getProperty("user.home"),"quickestquail"), "html");
      private final File xmlSourceFile = new File(rootDir, "movies_all.xml");
      private final File htmlSourceFile = new File(rootDir, "movies_all.html"); 
    
    public ViewAllMoviesHandler() throws FileNotFoundException
    {
    }
    
     
    private void buildXML()
    {
        Element rootElement = new Element("movies");
        
        for(Entry<File,String> en : DriveManager.get().getSelected().entrySet())
        {
            Movie mov = CachedMovieProvider.get().getMovieByID(en.getValue());
            
            Element movieElement = new Element("movie");
            
            movieElement.addContent(makeElement("imdbid", mov.getImdbID()));
            movieElement.addContent(makeElement("imdbrating", mov.getImdbRating()+""));
            movieElement.addContent(makeElement("imdbvotes", mov.getImdbVotes()+""));
            
            movieElement.addContent(makeElement("title", mov.getTitle()));  
            movieElement.addContent(makeElement("year", mov.getYear()+""));   
            movieElement.addContent(makeElement("countries","country", mov.getCountry())); 
            movieElement.addContent(makeElement("genres","genre", mov.getGenre())); 
            
            movieElement.addContent(makeElement("writers","writer", mov.getWriter()));  
            movieElement.addContent(makeElement("directors", "director", mov.getDirector()));
            movieElement.addContent(makeElement("actors", "actor", mov.getActors())); 
            
            movieElement.addContent(makeElement("poster", mov.getPoster()));    
            
            movieElement.addContent(makeElement("plot", mov.getPlot()));  
            
            movieElement.addContent(makeElement("file", en.getKey().getAbsolutePath()));
            
            rootElement.addContent(movieElement);
        }
        
        Document doc = new Document();
        doc.setRootElement(rootElement);
                
	XMLOutputter xmlOutput = new XMLOutputter();
	xmlOutput.setFormat(Format.getPrettyFormat());
                
        try {        
            if(!rootDir.exists())
                rootDir.mkdirs();
            xmlOutput.output(doc, new FileWriter(xmlSourceFile));
        } catch (IOException ex) {
            Logger.getLogger(ViewAllMoviesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
     private Element makeElement(String name, String val)
    {
       Element rootElement = new Element(name);
       rootElement.setText(val);
       return rootElement;
    }
    
    private Element makeElement(String name, String childName, String[] val)
    {
       Element rootElement = new Element(name);
       for(String v : val)
       {
           Element childElement  = new Element(childName);
           childElement.setText(v);
           rootElement.addContent(childElement);
       }
       return rootElement;        
    }
      
    private void buildHTML()
    {
        if(!xmlSourceFile.exists())
            buildXML();
              
        if(!htmlSourceFile.exists())
        {
            try {
                XSLT.xsl(new FileInputStream(xmlSourceFile), htmlSourceFile, getClass().getClassLoader().getResourceAsStream("html/style001.xsl"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ViewAllMoviesHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private String getHTML() throws FileNotFoundException
    {              
        Scanner sc = new Scanner(new FileInputStream(htmlSourceFile));
        String temp = "";
        while(sc.hasNextLine())
        {
            temp += sc.nextLine();
        }
        sc.close();
        return temp;
    }    
            
    @Override
    public void handle(HttpExchange he) throws IOException 
    {
        buildHTML();
        String response = getHTML();
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
}
