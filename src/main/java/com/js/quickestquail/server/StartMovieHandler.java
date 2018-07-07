/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.server;

import com.js.quickestquail.model.Drive;
import com.js.quickestquail.model.DriveManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;

/**
 *
 * @author joris
 */
public class StartMovieHandler implements HttpHandler{

    @Override
    public void handle(HttpExchange he) throws IOException {
        
        String imdbID = he.getRequestURI().toString();
        imdbID = imdbID.substring(imdbID.lastIndexOf("/") + 1);
       
        Drive d = DriveManager.get().getSelected();
        File selectedFile = null;
        if(d != null)
        {
            for(Entry<File,String> en : d.entrySet())
            {
                if(en.getValue().equals(imdbID))
                {
                    selectedFile = en.getKey();
                    break;
                }
            }
        }
        
        // play selected file
        // #TODO : use i18n IO to run system command
        System.out.println("Playing " + selectedFile.getAbsolutePath() + " ..");
        
        // send response
        he.sendResponseHeaders(200, 0);
        OutputStream os = he.getResponseBody();
        os.write(new byte[]{});
        os.close();      
    }
    
}
