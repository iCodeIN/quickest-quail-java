/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.model;

import com.js.pirategold.model.event.DefaultActionModel;
import com.js.pirategold.model.event.IActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author joris
 */

public class DriveManager extends DefaultActionModel{

    public static class DriveAddedEvent implements IActionEvent
    {        
        private Drive drive;
        public DriveAddedEvent(Drive d){this.drive = d;}
        public Drive getDrive() { return drive; }
    }
    
    public static class DriveRemovedEvent implements IActionEvent
    {  
        private Drive drive;
        public DriveRemovedEvent(Drive d){this.drive = d;}
        public Drive getDrive() { return drive; }        
    }
    
    public static class DriveSwitchedEvent implements IActionEvent
    {  
        private Drive drive;
        public DriveSwitchedEvent(Drive d){this.drive = d;}
        public Drive getDrive() { return drive; }        
    }    
    
    private static final DriveManager self = new DriveManager();
    private final List<Drive> drives = new ArrayList<>();
    private int selectedIndex;
      
    private DriveManager() {
        load();
    }

    public static DriveManager get() {
        return self;
    }
       
    public void load() {
        
        File rootDir = new File(System.getProperty("user.home"), "pirategold");
        File sourceFile = new File(rootDir, "drives.json");
        if (!sourceFile.exists()) {
            return;
        }
        
        try {
            // read file
            String temp = "";
            Scanner sc = new Scanner(sourceFile);
            while (sc.hasNextLine()) {
                temp += sc.nextLine();
            }
            sc.close();

            JSONArray arr = new JSONArray(temp);
            for(int i=0;i<arr.length();i++)
            {
                JSONObject obj = arr.getJSONObject(i);
                
                Map<String,Object> data = obj.getJSONObject("data").toMap();
                Map<File,String> dataInRightFormat = new HashMap<>();
                for(Entry<String,Object> en : data.entrySet())
                {
                    dataInRightFormat.put(new File(en.getKey()), en.getValue().toString());
                }
                File root = new File(obj.getString("root"));
                
                Drive d = new Drive(root, false);
                d.putAll(dataInRightFormat);
                
                add(d);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DriveManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void storeInThread() {
        new Thread() {
            @Override
            public void run() {
                store();
            }
        }.start();
    }

    public void printDebug()
    {
        for(Drive d : drives)
        {
            System.out.println(d.getRoot() + "\t" + d.size());
        }
    }
    
    public void store() {
        // build object to persist
        JSONArray arr = new JSONArray();
        for (Drive d : drives) {
            Map<String, String> m = new HashMap<>();
            for (Entry<File, String> en : d.entrySet()) {
                m.put(en.getKey().getAbsolutePath(), en.getValue());
            }
            JSONObject obj = new JSONObject();
            obj.put("data", m);
            obj.put("root", d.getRoot());
            arr.put(arr.length(), obj);
        }

        // persist
        File rootDir = new File(System.getProperty("user.home"), "pirategold");
        File sourceFile = new File(rootDir, "drives.json");
        FileWriter writer;
        try {
            writer = new FileWriter(sourceFile);
            writer.write(arr.toString(3));
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(DriveManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(Drive d) {
        drives.add(d);
        actionPerformed(new DriveAddedEvent(d));
    }

    public void remove(Drive d) {
        if(selectedIndex != -1 && d != null && drives.get(selectedIndex).getRoot().equals(d.getRoot()))
        {
            selectedIndex = -1;
            actionPerformed(new DriveSwitchedEvent(null));
        }
        drives.remove(d);        
        actionPerformed(new DriveRemovedEvent(d));
        storeInThread();
    }

    public Collection<Drive> all() {
        return drives;
    }

    public void setSelected(Drive d) {
        for (int i = 0; i < drives.size(); i++) {            
            if (drives.get(i).getRoot().equals(d.getRoot())) {
                selectedIndex = i;
                actionPerformed(new DriveSwitchedEvent(d));
                break;
            }
        }
    }

    public Drive getSelected() {
        if(selectedIndex == -1)
            return null;
        return drives.get(selectedIndex);
    }
}
