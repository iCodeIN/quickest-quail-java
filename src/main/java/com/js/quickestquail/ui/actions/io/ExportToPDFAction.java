/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.ui.actions.io;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.js.quickestquail.model.Drive;
import com.js.quickestquail.model.DriveManager;
import com.js.quickestquail.model.Movie;
import com.js.quickestquail.imdb.CachedMovieProvider;
import com.js.quickestquail.ui.JProgressDialog;
import com.js.quickestquail.ui.UI;
import com.js.quickestquail.ui.actions.AbstractIconAction;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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
public class ExportToPDFAction extends AbstractIconAction {

    public ExportToPDFAction() {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("export.pdf"), "img/011-pdf.png");
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

        File out = new File(fc.getSelectedFile(), "movies.pdf");

        try {
            writeAll(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportToPDFAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportToPDFAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeAll(File outputFile) throws FileNotFoundException, IOException {

        // progress dialog
        JProgressDialog dialog = new JProgressDialog(UI.get(), false);
        dialog.setMaximum(DriveManager.get().getSelected().size());
        dialog.setTitle(java.util.ResourceBundle.getBundle("i18n/i18n").getString("export.pdf"));
        dialog.setVisible(true);

        // run this in a new Thread
        new Thread() {
            @Override
            public void run() {
                try {
                    int nrOfMovies = 0;
                    List<Entry<File, String>> entries = new ArrayList<>(DriveManager.get().getSelected().entrySet());
                    java.util.Collections.sort(entries, new Comparator<Entry<File, String>>() {
                        @Override
                        public int compare(Entry<File, String> o1, Entry<File, String> o2) {
                            Movie mov1 = CachedMovieProvider.get().getMovieByID(o1.getValue());
                            Movie mov2 = CachedMovieProvider.get().getMovieByID(o2.getValue());
                            return mov1.getTitle().compareTo(mov2.getTitle());
                        }
                    });

                    PdfWriter writer = new PdfWriter(new FileOutputStream(outputFile));
                    PdfDocument pdf = new PdfDocument(writer);
                    Document doc = new Document(pdf);

                    for (Entry<File, String> en : entries) {
                        Movie mov = CachedMovieProvider.get().getMovieByID(en.getValue());

                        // update progress dialog
                        dialog.setText(mov.getTitle());
                        dialog.setProgress(nrOfMovies);

                        // add table
                        Table table = new Table(new float[]{0.5f, 0.25f, 0.25f});
                        table.setWidthPercent(100);
                        table.setBorder(Border.NO_BORDER);

                        Cell cell;
                        cell = new Cell(5, 1);
                        cell.setBorder(Border.NO_BORDER);

                        try {
                            Image img = new Image(ImageDataFactory.create(new URL(mov.getPoster())));
                            cell.setNextRenderer(new ImageBackgroundCellRenderer(cell, img));
                        } catch (Exception ex) {
                        }

                        cell.setHeight(160);
                        cell.setWidth(100);

                        table.addCell(cell);

                        cell = new Cell(1, 1).add("Title").setBorder(Border.NO_BORDER);
                        table.addCell(cell);
                        cell = new Cell(1, 1).add(mov.getTitle()).setBorder(Border.NO_BORDER);
                        table.addCell(cell);

                        cell = new Cell(1, 1).add("Year").setBorder(Border.NO_BORDER);
                        table.addCell(cell);
                        cell = new Cell(1, 1).add(mov.getYear() + "").setBorder(Border.NO_BORDER);
                        table.addCell(cell);

                        cell = new Cell(1, 1).add("IMDB ID").setBorder(Border.NO_BORDER);
                        table.addCell(cell);
                        cell = new Cell(1, 1).add(mov.getImdbID()).setBorder(Border.NO_BORDER);
                        table.addCell(cell);

                        cell = new Cell(1, 1).add("IMDB Rating").setBorder(Border.NO_BORDER);
                        table.addCell(cell);
                        cell = new Cell(1, 1).add(mov.getImdbRating() + "").setBorder(Border.NO_BORDER);
                        table.addCell(cell);

                        cell = new Cell(1, 1).add("IMDB Votes").setBorder(Border.NO_BORDER);
                        table.addCell(cell);
                        cell = new Cell(1, 1).add(mov.getImdbVotes() + "").setBorder(Border.NO_BORDER);
                        table.addCell(cell);

                        doc.add(table);

                        nrOfMovies++;
                        if (nrOfMovies % 4 == 0) {
                            doc.add(new AreaBreak());
                        }

                        doc.add(new Paragraph(""));

                    }

                    // close IO
                    doc.close();
                    pdf.close();
                    writer.close();

                    // close dialog
                    dialog.setVisible(false);
                    
                } catch (Exception ex) {
                }
            }
        }.start();

    }

}

class ImageBackgroundCellRenderer extends CellRenderer {

    protected Image img;

    public ImageBackgroundCellRenderer(Cell modelElement, Image img) {
        super(modelElement);
        this.img = img;
    }

    @Override
    public void draw(DrawContext drawContext) {
        img.scaleToFit(getOccupiedAreaBBox().getWidth(), getOccupiedAreaBBox().getHeight());
        drawContext.getCanvas().addXObject(img.getXObject(), getOccupiedAreaBBox());
        super.draw(drawContext);
    }
}
