/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui;

import com.js.pirategold.model.Drive;
import com.js.pirategold.model.Drive.MovieRemovedEvent;
import com.js.pirategold.model.DriveManager;
import com.js.pirategold.model.DriveManager.DriveAddedEvent;
import com.js.pirategold.model.DriveManager.DriveRemovedEvent;
import com.js.pirategold.model.DriveManager.DriveSwitchedEvent;
import com.js.pirategold.model.Movie;
import com.js.pirategold.model.event.IActionEvent;
import com.js.pirategold.model.event.IActionModelListener;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
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
public class UI extends JXFrame {

    private static final UI self = new UI();

    // components 
    private JTable similarMovieTable;
    private SearchableMovieTable driveMovieTable;

    private UI() {
        initComponents();
        initListeners();
    }

    private void initComponents() {
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
        tabs.add(java.util.ResourceBundle.getBundle("i18n/i18n").getString("tabs.drive"), driveMovieTable);
        tabs.add(java.util.ResourceBundle.getBundle("i18n/i18n").getString("tabs.similar"), new JScrollPane(similarMovieTable));

        add(tabs);

        // finishing touches
        setTitle(java.util.ResourceBundle.getBundle("i18n/i18n").getString("name"));
        pack();
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                DriveManager.get().store();
            }
        });
    }

    public JTable getDriveTable() {
        return driveMovieTable.getTable();
    }

    public JTable getSimilarMoviesTable() {
        return similarMovieTable;
    }

    private void initListeners() {      
        new UIActionModelListener();
    }

    public static UI get() {
        return self;
    }

}

class UIActionModelListener implements IActionModelListener {
  
    public UIActionModelListener()
    {   
        DriveManager.get().addListener(this);
        for(Drive d  : DriveManager.get().all())
            d.addListener(this);
    }
    
    private void driveAdded(Drive d) {
        d.addListener(this);
    }

    private void driveRemoved(Drive d) {        
        d.removeListener(this);
    }

    private void driveSwitched(Drive d) {
        List<Movie> movs = new ArrayList<>();
        if (d != null) {            
            for (Entry<File, String> en : d.entrySet()) {
                Movie mov = new Movie();
                mov.putAll(CachedOMDB.getMovie(en.getValue()));
                mov.put("file", en.getKey());                
                movs.add(mov);
            }
        }       
        MovieTableModel model = new MovieTableModel(movs);
        UI.get().getDriveTable().setModel(model);
    }

    private void movieRemoved(Movie m) {
        MovieTableModel model = (MovieTableModel) UI.get().getDriveTable().getModel();
        
        // remove row from table
        model.removeMovie(m);
        model.fireTableDataChanged();           
    }

    private void movieChanged(Movie m) {
        
    }

    @Override
    public void actionPerformed(IActionEvent event) {
        // Drive Events
        if(event instanceof DriveAddedEvent)
        {
            driveAdded(((DriveAddedEvent) event).getDrive());
        }
        else if(event instanceof DriveRemovedEvent)
        {
            driveRemoved(((DriveRemovedEvent) event).getDrive());            
        }
        else if(event instanceof DriveSwitchedEvent)
        {
            driveSwitched(((DriveSwitchedEvent) event).getDrive());            
        }
        // Movie Events
        else if(event instanceof MovieRemovedEvent)
        {
            movieRemoved(((MovieRemovedEvent) event).getMovie());            
        }
    }

}
