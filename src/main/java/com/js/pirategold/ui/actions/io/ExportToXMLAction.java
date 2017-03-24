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
import com.js.pirategold.server.ViewAllMoviesHandler;
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
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author joris
 */
public class ExportToXMLAction extends AbstractIconAction {

    public ExportToXMLAction() {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("export.xml"), "img/010-xml.png");
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

        File out = new File(fc.getSelectedFile(), "movies.xml");

        writeAll(out);

    }

    private void writeAll(File outputFile) {
        
        // progress dialog
        JProgressDialog dialog = new JProgressDialog(UI.get(), false);
        dialog.setMaximum(DriveManager.get().getSelected().size());
        dialog.setTitle("Export to XML");
        dialog.setVisible(true);
        
        // process in new thread
        new Thread() {
            @Override
            public void run() {
                try {
                    Element rootElement = new Element("movies");

                    List<Entry<File, String>> entries = new ArrayList<>(DriveManager.get().getSelected().entrySet());
                    java.util.Collections.sort(entries, new Comparator<Entry<File, String>>() {
                        @Override
                        public int compare(Entry<File, String> o1, Entry<File, String> o2) {
                            Movie mov1 = CachedOMDB.getMovie(o1.getValue());
                            Movie mov2 = CachedOMDB.getMovie(o2.getValue());
                            return mov1.getTitle().compareTo(mov2.getTitle());
                        }
                    });
                    
                    int nofMovies = 0;
                    for (Entry<File, String> en : entries) {
                        Movie mov = CachedOMDB.getMovie(en.getValue());

                        // update progress dialog
                        dialog.setText("Processing '" + mov.getTitle() + "'");
                        dialog.setProgress(nofMovies);                         
                        
                        Element movieElement = new Element("movie");

                        movieElement.addContent(makeElement("imdbid", mov.getImdbID()));
                        movieElement.addContent(makeElement("imdbrating", mov.getImdbRating() + ""));
                        movieElement.addContent(makeElement("imdbvotes", mov.getImdbVotes() + ""));

                        movieElement.addContent(makeElement("title", mov.getTitle()));
                        movieElement.addContent(makeElement("year", mov.getYear() + ""));
                        movieElement.addContent(makeElement("countries", "country", mov.getCountry()));
                        movieElement.addContent(makeElement("genres", "genre", mov.getGenre()));

                        movieElement.addContent(makeElement("writers", "writer", mov.getWriter()));
                        movieElement.addContent(makeElement("directors", "director", mov.getDirector()));
                        movieElement.addContent(makeElement("actors", "actor", mov.getActors()));

                        movieElement.addContent(makeElement("poster", mov.getPoster()));

                        movieElement.addContent(makeElement("plot", mov.getPlot()));

                        movieElement.addContent(makeElement("file", en.getKey().getAbsolutePath()));

                        rootElement.addContent(movieElement);
                        
                        nofMovies++;
                    }

                    Document doc = new Document();
                    doc.setRootElement(rootElement);

                    // close IO
                    XMLOutputter xmlOutput = new XMLOutputter();
                    xmlOutput.setFormat(Format.getPrettyFormat());                    
                    xmlOutput.output(doc, new FileWriter(outputFile));

                    // close dialog
                    dialog.setVisible(false);
                    
                } catch (Exception ex) {
                }
            }
        }.start();
    }

    private Element makeElement(String name, String val) {
        Element rootElement = new Element(name);
        rootElement.setText(val);
        return rootElement;
    }

    private Element makeElement(String name, String childName, String[] val) {
        Element rootElement = new Element(name);
        for (String v : val) {
            Element childElement = new Element(childName);
            childElement.setText(v);
            rootElement.addContent(childElement);
        }
        return rootElement;
    }
}
