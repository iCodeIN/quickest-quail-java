/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.ui;

import com.js.pirategold.model.Movie;
import com.js.pirategold.omdb.CachedOMDB;
import com.js.pirategold.query.AbstractSyntaxTree;
import com.js.pirategold.query.AbstractSyntaxTree.AbstractSyntaxTreeNode;
import com.js.pirategold.query.Evaluator;
import com.js.pirategold.query.Postfix;
import com.js.pirategold.query.Tokenizer;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultRowSorter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

/**
 *
 * @author joris
 */
public class SearchableMovieTable extends JPanel{
    
    private JTable table;
    private JTextField textField;
    
    public SearchableMovieTable()
    {
        initComponents();
    }
    
    private void initComponents()
    {
        table = new JTable();
        table.setModel(new MovieTableModel());
        table.setAutoCreateRowSorter(true);
        
        textField = new JTextField();
        
        textField.addKeyListener(new KeyAdapter(){     
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    filter(textField.getText());
                }
            }            
        });
        
        setLayout(new BorderLayout());
        add(textField, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
               
    private void filter(String query)
    {
        DefaultRowSorter sorter = (DefaultRowSorter) table.getRowSorter();
        if(query.isEmpty())
            sorter.setRowFilter(null);
        else
            sorter.setRowFilter(new MyRowFilter(query));
    }
        
    public JTable getTable()
    {
        return table;
    }
    
    public JTextField getTextField()
    {
        return textField;
    }
    
}

class MyRowFilter extends RowFilter<MovieTableModel, Object>
{

    private AbstractSyntaxTreeNode root;
    
    public MyRowFilter(String query)
    {
        root = AbstractSyntaxTree.buildAST(Postfix.toPostfix(Tokenizer.tokenize(query)));
    }
    
    @Override
    public boolean include(Entry<? extends MovieTableModel, ? extends Object> entry) {
       String imdbId = entry.getStringValue(8);       
       Movie mov = CachedOMDB.getMovie(imdbId);
       
       // set variables
       Map<String,Object> vars = new HashMap<>();
       // "title","year","imdbid","plot","director","writer","actors","imdbrating","imdbvotes","language","country","genre"
       vars.put("title", mov.getTitle());
       vars.put("year", mov.getYear());
       
       vars.put("imdbid", mov.getImdbID());
       vars.put("imdbrating", mov.getImdbRating());
       vars.put("imdbvotes",mov.getImdbVotes());
       vars.put("metascore", mov.getMetaScore());
       
       vars.put("plot", mov.getPlot());

       vars.put("director", mov.getDirector());
       vars.put("writer", mov.getWriter());
       vars.put("actors", mov.getActors());
       
       vars.put("country", mov.getCountry());
       vars.put("genre", mov.getGenre());
       vars.put("language", mov.getLanguage());
       
       // evaluate
       if(root == null)
           return true;
       
       Object eval = null;
       try{
        eval = Evaluator.evaluate(root, vars);
       }catch(Exception ex){}
       
       return (eval instanceof Boolean) ? (Boolean) eval : false;
       
    }

}
