/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.io;

import com.js.pirategold.model.Movie;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joris
 */
public class AggregateFileHandler implements IFileHandler{

    private final List<IFileHandler> handlers = new ArrayList<>();
    
    public AggregateFileHandler()
    {       
    }
    
    public void add(IFileHandler handler)
    {
        handlers.add(handler);
    };
            
    @Override
    public Movie process(File f) {
        for(IFileHandler handler : handlers)
        {
            Movie mov = handler.process(f);
            if(mov != null)
                return mov;
        }
        return new Movie();
    }
    
}
