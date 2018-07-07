/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author joris
 */
public class Tokenizer {
    
    private static final String[] VARIABLES = {"title","year","imdbid","plot","director","writer","actors","imdbrating","imdbvotes","language","country","genre","metascore"};
    private static final String[] OPERANDS = {"*","/","%","+","-",">","<","<=",">=","==","!=","and","or","not","contains","startswith","endswith","size"};
    
    public static enum TokenType
    {
        NUMBER,     
        OPERATOR,     
        PARENTHESIS_LEFT,
        PARENTHESIS_RIGHT,
        SPACE,
        STRING,
        VARIABLE
    }
    
    public static class Token
    {
        public String text = "";
        public TokenType type = null;
    }
    
    public static List<Token> tokenize(String s)
    {
        s = s.toUpperCase().toLowerCase();
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while(i < s.length())
        {                        
            int[] nxtPos = {nextNumber(s,i), nextOperand(s,i), nextParenthesisLeft(s,i), nextParenthesisRight(s,i), nextSpace(s,i), nextString(s,i), nextVariable(s,i)};
            int maxIndex = 0;           
            for(int j=0;j<nxtPos.length;j++)
            {
                if(nxtPos[j] > nxtPos[maxIndex])
                    maxIndex = j;
            }
            
            // build token
            Token t = new Token();
            t.text = s.substring(i, nxtPos[maxIndex]);
            switch(maxIndex)
            {
                case 0:
                    t.type = TokenType.NUMBER;
                    break;
                case 1:
                    t.type = TokenType.OPERATOR;
                    break;
                case 2:
                    t.type = TokenType.PARENTHESIS_LEFT;
                    break;
                case 3:
                    t.type = TokenType.PARENTHESIS_RIGHT;
                    break;                    
                case 4:
                    t.type = TokenType.SPACE;
                    break;
                case 5:
                    t.type = TokenType.STRING;
                    break;
                case 6:
                    t.type = TokenType.VARIABLE;
                    break;
            }
            tokens.add(t);
            
            if(nxtPos[maxIndex] <= i)
                throw new IllegalArgumentException("Invalid input");
            i = nxtPos[maxIndex];
        }
        return tokens;
    }
    
    private static int nextSpace(String s, int p)
    {
        int k = p;
        while(k < s.length() && Character.isWhitespace(s.charAt(k)))
            k++;
        return k;
    }
    
    private static int nextParenthesisLeft(String s, int p)
    {
        return s.charAt(p) == '(' ? p+1 : p;          
    }
    
    private static int nextParenthesisRight(String s, int p)
    {
        return s.charAt(p) == ')' ? p+1 : p;           
    }
    
    private static int nextVariable(String s, int p)
    {
        int k = p;
        String subs = s.substring(p);
        for(String v : VARIABLES)
        {
            if(subs.startsWith(v))
            {
                k = java.lang.Math.max(k, p+v.length());
            }
        }
        return k;
    }
    
    private static int nextNumber(String s, int p)
    {
        String subs = s.substring(p);
        
        Pattern intPattern = Pattern.compile("([0123456789]+).*");
        Pattern floatPattern = Pattern.compile("([0123456789]*\\.[0123456789]+).*");
    
        Matcher floatMatcher = floatPattern.matcher(subs);
        if(floatMatcher.matches())
        {
            return p + floatMatcher.group(1).length();
        }
        
        Matcher intMatcher = intPattern.matcher(subs);
        if(intMatcher.matches())
        {
            return p + intMatcher.group(1).length();
        }
       
        
        return p;
    }
    
    private static int nextString(String s, int p)
    {
        if(s.charAt(p) != '"')
            return p;
       return s.indexOf('"', p+1) + 1;
    }
    
    private static int nextOperand(String s, int p)
    {
        int k = p;
        String subs = s.substring(p);
        for(String o : OPERANDS)
        {
            if(subs.startsWith(o))
            {
                k = java.lang.Math.max(k, p+o.length());
            }
        }
        return k;
    }
}
