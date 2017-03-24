/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.actions.drive;

import com.js.pirategold.model.Drive;
import com.js.pirategold.model.DriveManager;
import com.js.pirategold.ui.JProgressDialog;
import com.js.pirategold.ui.UI;
import com.js.pirategold.ui.actions.AbstractIconAction;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author joris
 */
public class AddDriveAction extends AbstractIconAction {

    public AddDriveAction() {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("drive.add"), "img/004-plus.png");;
    }

    private boolean isParentFile(File parent, File child) {
        while (child.getParentFile() != null) {
            File p = child.getParentFile();
            if (parent.equals(p)) {
                return true;
            }
            child = p;
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // open a file chooser
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retval = fc.showOpenDialog(null);

        if (retval != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File dir = fc.getSelectedFile();

        for (Drive d : DriveManager.get().all()) {
            if (d.getRoot().equals(dir)) {
                // show error dialog
                JOptionPane.showMessageDialog(UI.get(),
                        java.util.ResourceBundle.getBundle("i18n/i18n").getString("drive.drivealreadyadded"),
                        "PirateGold",
                        JOptionPane.WARNING_MESSAGE);
                // return
                return;
            }
            if (isParentFile(d.getRoot(), dir)) {
                // show error dialog
                JOptionPane.showMessageDialog(UI.get(),
                        java.util.ResourceBundle.getBundle("i18n/i18n").getString("drive.drivehasregisteredparent"),
                        "PirateGold",
                        JOptionPane.WARNING_MESSAGE);
                // return
                return;
            }
        }

        // progress dialog
        JProgressDialog dialog = new JProgressDialog(UI.get(), false);
        dialog.setIndeterminate(true);
        dialog.setTitle("Add Drive");
        dialog.setVisible(true);
        
        // run in separate thread
        new Thread() {
            @Override
            public void run() {
                // create new drive object        
                Drive d = new Drive(dir);

                // persist
                DriveManager.get().add(d);

                // switch to drive
                DriveManager.get().setSelected(d);
                
                // close dialog
                dialog.setVisible(false);
            }
        }.start();

    }

}
