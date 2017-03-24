/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joris
 */
public class ViewDetailsHandlers implements HttpHandler {
    
    private final File rootDir = new File(new File(System.getProperty("user.home"),"pirategold"), "html");   
    private final File xmlSourceFile = new File(rootDir, "movies_all.xml");

    private File buildHTML(String imdbID) {
        
        File htmlSourceFile = new File(rootDir, "movies_" + imdbID + ".html");
        
        // build html file if needed
        if (!htmlSourceFile.exists()) {
            try {
                Map<String,String> params = new HashMap<>();
                params.put("imdbID", imdbID);
                XSLT.xsl(new FileInputStream(xmlSourceFile), htmlSourceFile, getClass().getClassLoader().getResourceAsStream("html/style002.xsl"), params);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ViewAllMoviesHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // return
        return htmlSourceFile;
    }

    private String readHTML(File inputFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileInputStream(inputFile));
        String temp = "";
        while (sc.hasNextLine()) {
            temp += sc.nextLine();
        }
        sc.close();
        return temp;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        
        String imdbID = he.getRequestURI().toString();
        imdbID = imdbID.substring(imdbID.lastIndexOf("/") + 1);
       
        String response = readHTML(buildHTML(imdbID));
        
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();       

    }

}
