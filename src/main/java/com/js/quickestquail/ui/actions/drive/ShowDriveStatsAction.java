/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.ui.actions.drive;

import com.js.quickestquail.model.DriveManager;
import com.js.quickestquail.ui.UI;
import com.js.quickestquail.ui.actions.AbstractIconAction;
import com.js.quickestquail.ui.stats.StatFrame;
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
