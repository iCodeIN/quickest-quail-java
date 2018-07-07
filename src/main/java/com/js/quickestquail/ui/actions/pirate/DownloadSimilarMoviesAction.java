/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.ui.actions.pirate;

import com.js.quickestquail.ui.actions.AbstractIconAction;
import java.awt.event.ActionEvent;

/**
 *
 * @author joris
 */
public class DownloadSimilarMoviesAction extends AbstractIconAction{

    public DownloadSimilarMoviesAction()
    {
        super(java.util.ResourceBundle.getBundle("i18n/i18n").getString("pirate.downloadsimilar"),"img/008-poison.png");
    }
  
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
