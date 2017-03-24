/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.omdb;

/**
 *
 * @author joris
 */
public class DefaultFileHandler extends AggregateFileHandler{
    
    private static final DefaultFileHandler self = new DefaultFileHandler();
    
    private DefaultFileHandler()
    {
        add(new RegexFileHandler());
    };
    
    public static IFileHandler get()
    {
        return self;
    }
}
