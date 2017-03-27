/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui;

import com.js.pirategold.model.Drive;
import com.js.pirategold.model.DriveManager;
import com.js.pirategold.model.Movie;
import com.js.pirategold.omdb.CachedOMDB;
import com.js.pirategold.ui.actions.drive.AddDriveAction;
import com.js.pirategold.ui.actions.drive.RemoveDriveAction;
import com.js.pirategold.ui.actions.drive.ScanDriveAction;
import com.js.pirategold.ui.actions.drive.ShowDriveStatsAction;
import com.js.pirategold.ui.actions.drive.SwitchDriveAction;
import com.js.pirategold.ui.actions.io.ExportToPDFAction;
import com.js.pirategold.ui.actions.io.ExportToTXTAction;
import com.js.pirategold.ui.actions.io.ExportToXMLAction;
import com.js.pirategold.ui.actions.pirate.DownloadSimilarMoviesAction;
import com.js.pirategold.ui.actions.pirate.ShowSimilarMoviesAction;
import com.js.pirategold.ui.actions.server.ToggleServerAction;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

/**
 *
 * @author joris
 */
public class UI extends JXFrame{
    
    private static final UI self = new UI();
    
    // components 
    private JTable similarMovieTable;
    private SearchableMovieTable driveMovieTable;
    
    private UI()
    {
        initComponents();
        initListeners();
    }
    
    private void initComponents()
    {
        // build JXTaskPaneContainer
        JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
        
        // taskpane related to "drive"
        JXTaskPane drivePane = new JXTaskPane();    
        drivePane.setTitle(java.util.ResourceBundle.getBundle("i18n/i18n").getString("drive"));
        drivePane.add(new AddDriveAction());
        drivePane.add(new RemoveDriveAction());
        drivePane.add(new ScanDriveAction());
        drivePane.add(new SwitchDriveAction());
        drivePane.add(new ShowDriveStatsAction());
        taskPaneContainer.add(drivePane);
        
        // taskpane related to IO
        JXTaskPane ioPane = new JXTaskPane();
        ioPane.setTitle(java.util.ResourceBundle.getBundle("i18n/i18n").getString("export"));
        ioPane.add(new ExportToPDFAction());
        ioPane.add(new ExportToTXTAction());      
        ioPane.add(new ExportToXMLAction());    
        taskPaneContainer.add(ioPane);        
        
        // taskpane related to "less than legal" activities
        JXTaskPane piratePane = new JXTaskPane();
        piratePane.setTitle(java.util.ResourceBundle.getBundle("i18n/i18n").getString("pirate"));
        piratePane.add(new ShowSimilarMoviesAction());
        piratePane.add(new DownloadSimilarMoviesAction());      
        taskPaneContainer.add(piratePane);
        
        // taskpane related to "server"
        JXTaskPane serverPane = new JXTaskPane();
        serverPane.setTitle(java.util.ResourceBundle.getBundle("i18n/i18n").getString("server"));
        serverPane.add(new ToggleServerAction());     
        taskPaneContainer.add(serverPane);  
        
        add(taskPaneContainer, BorderLayout.EAST);
        
        // build JTable(s)
        similarMovieTable = new JTable(new MovieTableModel());       
        similarMovieTable.setAutoCreateRowSorter(true);        
        driveMovieTable = new SearchableMovieTable();
        
        // tabs
        JTabbedPane tabs = new JTabbedPane();             
        tabs.add("Drive", driveMovieTable);
        tabs.add("Similar", new JScrollPane(similarMovieTable));
        
        add(tabs);
        
        // finishing touches
        setTitle("PirateGold");        
        pack();        
    }
    
    public JTable getDriveTable()
    {
        return driveMovieTable.getTable();
    }
    
    public JTable getSimilarMoviesTable()
    {
        return similarMovieTable;
    }
          
    private void initListeners()
    {
        DriveManager.get().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Drive d = DriveManager.get().getSelected();
                if(d == null)
                {
                    driveMovieTable.getTable().setModel(new MovieTableModel());
                    return;
                }
                List<Movie> movs = new ArrayList<>();
                if(d != null)
                {
                    for(String id : d.values())
                    {
                        movs.add(CachedOMDB.getMovie(id));
                    }
                }
                MovieTableModel model = new MovieTableModel(movs);
                driveMovieTable.getTable().setModel(model);
            }
        });        
    }
    
    public static UI get() { return self; }
    
}
