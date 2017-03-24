/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.actions.drive;

import com.js.pirategold.model.DriveManager;
import com.js.pirategold.ui.UI;
import com.js.pirategold.ui.actions.AbstractIconAction;
import com.js.pirategold.ui.stats.StatFrame;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;

/**
 *
 * @author joris
 */
public class ShowDriveStatsAction extends AbstractIconAction{

    public ShowDriveStatsAction()
    {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("drive.stats"),"img/005-chart.png");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame frame = new StatFrame(UI.get());
        frame.setTitle(DriveManager.get().getSelected().getRoot().toString());
        frame.setVisible(true);
    }
    
}
