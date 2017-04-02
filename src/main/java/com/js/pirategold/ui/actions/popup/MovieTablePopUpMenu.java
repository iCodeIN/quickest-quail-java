/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui.actions.popup;

import com.js.pirategold.ui.MovieTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 *
 * @author joris
 */
public class MovieTablePopUpMenu extends JPopupMenu {

    private JTable table;

    public MovieTablePopUpMenu(JTable table) {
        add(new DeleteMovieAction(table));
        add(new PlayMovieAction(table));
        add(new RetagMovieAction(table));
        
        this.table = table;
        table.addMouseListener(new MousePopupListener(this));
    }

    public JTable getJTable()
    {
        return table;
    }
    
}

// An inner class to check whether mouse events are the popup trigger
class MousePopupListener extends MouseAdapter {

    private MovieTablePopUpMenu popup;
    
    public MousePopupListener(MovieTablePopUpMenu popupMenu)
    {
        this.popup = popupMenu;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        checkPopup(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        checkPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        checkPopup(e);
    }

    private void checkPopup(MouseEvent e) {
        if (e.isPopupTrigger() && popup.getJTable().getSelectedRows().length == 1) {
            popup.show(popup.getJTable(), e.getX(), e.getY());
        }
    }
}
