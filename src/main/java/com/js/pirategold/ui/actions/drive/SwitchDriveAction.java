/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.actions.drive;

import com.js.pirategold.model.Drive;
import com.js.pirategold.model.DriveManager;
import com.js.pirategold.ui.actions.AbstractIconAction;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author joris
 */
public class SwitchDriveAction extends AbstractIconAction{

    public SwitchDriveAction()
    {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("drive.switch"),"img/002-switch.png");
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

        
        Drive selectedDrive = null;
        for(Drive d : drives)
        {
            if(d.getRoot().getAbsolutePath().equals(selectedPath))
            {
                selectedDrive = d;
            }
        }
        
        // switch to a different drive
        if(selectedDrive != null)
            DriveManager.get().setSelected(selectedDrive);
    }
    
}
