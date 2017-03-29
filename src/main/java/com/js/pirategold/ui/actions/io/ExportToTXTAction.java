/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.actions.io;

import com.js.pirategold.model.Drive;
import com.js.pirategold.model.DriveManager;
import com.js.pirategold.model.Movie;
import com.js.pirategold.omdb.CachedOMDB;
import com.js.pirategold.ui.JProgressDialog;
import com.js.pirategold.ui.UI;
import com.js.pirategold.ui.actions.AbstractIconAction;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author joris
 */
public class ExportToTXTAction extends AbstractIconAction {

    public ExportToTXTAction() {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("export.txt"), "img/012-txt.png");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Drive d = DriveManager.get().getSelected();
        if (d == null) {
            return;
        }

        if (d.isEmpty()) {
            return;
        }

        // open a file chooser
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retval = fc.showSaveDialog(UI.get());

        if (retval != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File out = new File(fc.getSelectedFile(), "movies.txt");

        try {
            writeAll(out);
        } catch (IOException ex) {
            Logger.getLogger(ExportToTXTAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeAll(File outputFile) throws IOException {

        // progress dialog
        JProgressDialog dialog = new JProgressDialog(UI.get(), false);
        dialog.setMaximum(DriveManager.get().getSelected().size());
        dialog.setTitle(java.util.ResourceBundle.getBundle("i18n/i18n").getString("export.txt"));
        dialog.setVisible(true);

        // run export in new thread
        new Thread() {
            @Override
            public void run() {
                try {
                    List<Entry<File, String>> entries = new ArrayList<>(DriveManager.get().getSelected().entrySet());
                    java.util.Collections.sort(entries, new Comparator<Entry<File, String>>() {
                        @Override
                        public int compare(Entry<File, String> o1, Entry<File, String> o2) {
                            Movie mov1 = CachedOMDB.getMovie(o1.getValue());
                            Movie mov2 = CachedOMDB.getMovie(o2.getValue());
                            return mov1.getTitle().compareTo(mov2.getTitle());
                        }
                    });

                    FileWriter fw = new FileWriter(outputFile);
                    int pad = 11;
                    int nofMovies = 0;
                    for (Entry<File, String> en : entries) {
                        Movie mov = CachedOMDB.getMovie(en.getValue());

                        // update progress dialog
                        dialog.setText(mov.getTitle());
                        dialog.setProgress(nofMovies);                        
                        
                        fw.write(pad("Title", pad) + " : " + mov.getTitle() + "\n");
                        fw.write(pad("Year", pad) + " : " + mov.getYear() + "\n");
                        fw.write(pad("Country", pad) + " : " + arrayToString(mov.getCountry()) + "\n");

                        fw.write(pad("Genre", pad) + " : " + arrayToString(mov.getGenre()) + "\n");
                        fw.write(pad("Director", pad) + " : " + arrayToString(mov.getDirector()) + "\n");
                        fw.write(pad("Writer", pad) + " : " + arrayToString(mov.getWriter()) + "\n");
                        fw.write(pad("Actors", pad) + " : " + arrayToString(mov.getActors()) + "\n");

                        fw.write(pad("IMDB ID", pad) + " : " + mov.getImdbID() + "\n");
                        fw.write(pad("IMDB Rating", pad) + " : " + mov.getImdbRating() + "\n");
                        fw.write(pad("IMDB Votes", pad) + " : " + mov.getImdbVotes() + "\n");
                        fw.write(pad("Metascore", pad) + " : " + mov.getMetaScore() + "\n");

                        fw.write(pad("Plot", pad) + " : " + mov.getPlot() + "\n");

                        fw.write(pad("Poster", pad) + " : " + mov.getPoster() + "\n");

                        fw.write(pad("File", pad) + " : " + en.getKey().getAbsolutePath() + "\n");

                        fw.write("\n");
                        nofMovies++;
                    }
                    
                    // close stream
                    fw.flush();
                    fw.close();
                    
                    // close dialog
                    dialog.setVisible(false);
                    
                } catch (Exception ex) {
                }
            }
        }.start();

    }

    private String arrayToString(String[] arr) {
        String s = "";
        for (int i = 0; i < arr.length; i++) {
            s += (i == 0 ? "" : ", ") + arr[i];
        }
        return s;
    }

    private String pad(String s, int n) {
        while (s.length() < n) {
            s += " ";
        }
        return s;
    }
}
