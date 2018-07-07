/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.ui;

import com.js.quickestquail.model.Movie;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author joris
 */
public class MovieTableModel extends DefaultTableModel{
 
    private List<Movie> movies = new ArrayList<>();
    
    private static final String[] COLUMN_NAMES = {  java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.title"), 
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.year"), 
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.genre"),
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.country"),
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.writer"),
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.director"),
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.language"),
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.actors"),
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.imdbid"),
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.imdbrating"),
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.imdbvotes"),
                                                    java.util.ResourceBundle.getBundle("i18n/i18n").getString("table.metascore")};
    private static final Class[] COLUMN_CLASSES = {String.class, Integer.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Double.class, Integer.class, Double.class};
    
    public MovieTableModel()
    {        
    }
    
    public MovieTableModel(List<Movie> movies)
    {
        this.movies.addAll(movies);
    }

    @Override
    public int getRowCount() {
        return movies == null ? 0 : movies.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES == null ? 0 : COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
       Movie m = movies.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return m.getTitle();
            case 1:
                return m.getYear();
            case 2:
                return arrayToString(m.getGenre());
            case 3:
                return arrayToString(m.getCountry());
            case 4:
                return arrayToString(m.getWriter());
            case 5:
                return arrayToString(m.getDirector());
            case 6:
                return arrayToString(m.getLanguage());
            case 7:
                return arrayToString(m.getActors());   
            case 8:
                return m.getImdbID();
            case 9:
                return m.getImdbRating();
            case 10:
                return m.getImdbVotes();
            case 11:
                return m.getMetaScore();
            default:
                break;
        }
       // unhappy flow
       return null;
    }

    public void removeMovie(Movie mov)
    {
        movies.remove(mov);
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {     
    }
   
    private String arrayToString(String[] arr)
    {
       String s = "";
       for(int i=0;i<arr.length;i++)
       {
           s += (( i == 0 ? "" : ", ") + arr[i]);
       }
       return s;
    }

    public Movie getMovieAt(int rowIndex) {
        if(rowIndex < 0 || rowIndex >= getRowCount())
            return null;
         return movies.get(rowIndex);
    }
    
}
