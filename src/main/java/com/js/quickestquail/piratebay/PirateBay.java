/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.piratebay;

import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author joris
 */
public class PirateBay {
    
    public static void search(String query) throws IOException
    {
        String url = "https://pirateproxy.cat/search/" + query;
        System.out.println(url);
        
        Document doc = Jsoup.parse(new URL(url), 10000);
        Element table = doc.select("table#searchResult").get(0);
        
        Elements hrefs = table.select("a[href]");
        for(Element link : hrefs)
        {
          String linkTarget = link.attr("href");
          System.out.println(linkTarget);
        }
        
    }
}
