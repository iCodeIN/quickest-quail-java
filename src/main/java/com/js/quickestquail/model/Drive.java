/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.model;

import com.js.quickestquail.model.event.DefaultActionModel;
import com.js.quickestquail.model.event.IActionEvent;
import com.js.quickestquail.model.event.IActionModel;
import com.js.quickestquail.model.event.IActionModelListener;
import com.js.quickestquail.io.DefaultFileHandler;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author joris
 */

public class Drive extends HashMap<File,String> implements IActionModel{

    public static class MovieRemovedEvent implements IActionEvent
    {
        private Movie movie;
        public MovieRemovedEvent(Movie mov){this.movie = mov;}
        public Movie getMovie() { return movie; }
    }
    
    private final File root;
    private final String[] MEDIA_EXTENSIONS = {"avi","mp4","mpeg4","flv","mkv","mov","wmv","ogv","ogg","yuv","m4v"};
    private DefaultActionModel actionModel = new DefaultActionModel();
    
    public Drive(File root)
    {
        this(root, true);
    }
    
    public Drive(File root, boolean scan)
    {
        this.root = root;
        if(scan)
            explore(root, new HashSet<>());
    }
    
    public File getRoot()
    {
        return root;
    }
    
    private void explore(File f, Set<File> explored)
    {
        if(explored.contains(f))
            return;
        explored.add(f);
        if(f.isDirectory())
        {
            for(File subDir : f.listFiles())
            {
                explore(subDir, explored);
            }
        }
        else
        {
            processFile(f);
        }
    }
    
    private void processFile(File f)
    {
        // check extension
        String name = f.getName();
        String extension = name.contains(".") ? name.substring(name.lastIndexOf(".")+1) : "";
        if(!isMediaExtension(extension))
            return;
    
        // delegate call to DefaultFileHandler
        Movie mov = DefaultFileHandler.get().process(f);
        
        // if a valid movie is returned, add it
        if(!mov.getImdbID().isEmpty())
            put(f, mov.getImdbID());
    }
    
    private boolean isMediaExtension(String s)
    {
        s = s.toUpperCase();        
        for(int i=0;i<MEDIA_EXTENSIONS.length;i++)
        {
            if(MEDIA_EXTENSIONS[i].equalsIgnoreCase(s))
                return true;
        }
        return false;
    }

    public void rescan() {
        Set<File> alreadyDone = new HashSet<>(keySet());
        explore(root, alreadyDone);        
    }

    @Override
    public void addListener(IActionModelListener listener) {
        actionModel.addListener(listener);
    }

    @Override
    public void addListener(IActionModelListener listener, Class eventType) {
        actionModel.addListener(listener, eventType);
    }

    @Override
    public void removeListener(IActionModelListener listener) {
        actionModel.removeListener(listener);
    }

    @Override
    public void removeListener(IActionModelListener listener, Class eventType) {
        actionModel.removeListener(listener, eventType);
    }

    @Override
    public void actionPerformed(IActionEvent event) {
        actionModel.actionPerformed(event);
    }
}
