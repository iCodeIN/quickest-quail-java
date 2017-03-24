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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author joris
 */
public class ScanDriveAction extends AbstractIconAction{
    
    public ScanDriveAction()
    {
        // super("scan drive","");
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("drive.scan"), "img/001-fingerprint.png");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // get all drives
        List<Drive> drives = new ArrayList<>(DriveManager.get().all());
        String[] options = new String[drives.size()];
        for(int i=0;i<drives.size();i++)
        {
            options[i] = drives.get(i).getRoot().getAbsolutePath();
        }
        
        // show dialog
        String selectedPath = (String) JOptionPane.showInputDialog(
                                        null,
                                        "Select a drive:",
                                        "PirateGold",
                                        JOptionPane.PLAIN_MESSAGE,
                                        null,
                                        options,
                                        options.length == 0 ? "" : options[0]);

        
        Drive tempDrive = null;
        for(Drive d : drives)
        {
            if(d.getRoot().getAbsolutePath().equals(selectedPath))
            {
                tempDrive = d;
            }
        }
        
        if(tempDrive == null)
            return;
        
        // progress dialog
        JProgressDialog dialog = new JProgressDialog(UI.get(), false);
        dialog.setIndeterminate(true);
        dialog.setTitle("Scan Drive");
        dialog.setVisible(true);        
        
        final Drive selectedDrive = tempDrive;
        new Thread()
        {
            @Override
            public void run()        
            {
                selectedDrive.rescan();
                DriveManager.get().actionPerformed();
                dialog.setVisible(false);
            }
        }.start();
    }
    
}
