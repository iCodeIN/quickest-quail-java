/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

/**
 *
 * @author joris
 */
public class XSLT {

    public static void xsl(InputStream xmlIn, File htmlOut, InputStream xslIn) {
        Map<String,String> empty = new HashMap<>();
        xsl(xmlIn, htmlOut, xslIn, empty);
    }
    
    // This method applies the xslFilename to inFilename and writes
    // the output to outFilename.
    public static void xsl(InputStream xmlIn, File htmlOut, InputStream xslIn, Map<String,String> params) {
        try {
            // Create transformer factory
            TransformerFactory factory = TransformerFactory.newInstance();
            
            // Use the factory to create a template containing the xsl file
            Templates template = factory.newTemplates(new StreamSource(xslIn));
            
            // Use the template to create a transformer
            Transformer xformer = template.newTransformer();            
            for(Entry<String,String> en : params.entrySet())
            {
                xformer.setParameter(en.getKey(), en.getValue());
            }
            
            // Prepare the input and output files
            Source source = new StreamSource(xmlIn);
            Result result = new StreamResult(new FileOutputStream(htmlOut));

            // Apply the xsl file to the source file and write the result to the output file
            xformer.transform(source, result);

        } catch (FileNotFoundException e) {
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }

}
