/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.model;

import java.util.Collection;

/**
 *
 * @author joris
 */
public interface ISimilarMovieFinder {
    
    public Collection<String> similar(Collection<String> ids);
    
}
