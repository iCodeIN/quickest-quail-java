/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 *
 * @author joris
 */
public class MyFlixServer {
          
    private static final MyFlixServer self = new MyFlixServer();
    
    private boolean isRunning  = false;
    private HttpServer server;
    
    private MyFlixServer(){}
    
    public static MyFlixServer get() { return self; }
    
    public boolean isRunning() 
    {
        return isRunning;
    }
    
    public void start() throws IOException
    {
        if(isRunning)
            return;
        isRunning = true;
        
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new ViewAllMoviesHandler());
        server.createContext("/movie", new ViewDetailsHandlers());
        server.createContext("/play", new StartMovieHandler());
        server.setExecutor(null); // creates a default executor
        server.start();        
    }
    
    public void stop()
    {
        if(!isRunning)
            return;
        isRunning = false;
        server.stop(0);
    }
}
