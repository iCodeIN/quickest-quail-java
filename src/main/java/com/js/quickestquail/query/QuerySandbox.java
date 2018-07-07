/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.query;

import com.js.quickestquail.model.Movie;
import com.js.quickestquail.imdb.CachedMovieProvider;
import com.js.quickestquail.query.AbstractSyntaxTree.AbstractSyntaxTreeNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.graphstream.graph.Graph;

/**
 *
 * @author joris
 */
public class QuerySandbox {
 
    public static void main(String[] args)
    {
        List<Tokenizer.Token> tokens = Tokenizer.tokenize("(metascore/10) * imdbrating >= 25 AND year >= 2010 AND language contains \"English\"");
        tokens = Postfix.toPostfix(tokens);
        
        for(int i=0;i<tokens.size();i++)
            System.out.println(i + "\t" + tokens.get(i).type + "\t\t" + tokens.get(i).text);
        
        AbstractSyntaxTreeNode ast = AbstractSyntaxTree.buildAST(tokens);
       
        Movie mov = CachedMovieProvider.get().getMovie("tt2294629");
        
        Map<String, Object> vars = new HashMap<>();
        vars.put("genre", mov.getGenre());
        vars.put("country", mov.getCountry());
        vars.put("metascore", mov.getMetaScore());
        vars.put("imdbrating", mov.getImdbRating());
        vars.put("year", mov.getYear());
        vars.put("language", mov.getLanguage());
        
        System.out.println(Evaluator.evaluate(ast, vars));
        
        Graph g = ASTVisualizer.buildGraph(ast);
        g.display();
        
    }
}
