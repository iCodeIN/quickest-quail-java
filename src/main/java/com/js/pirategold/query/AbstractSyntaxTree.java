/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author joris
 */
public class AbstractSyntaxTree {
    
    public static class AbstractSyntaxTreeNode
    {
        public Tokenizer.Token token;
        
        // linkage
        public AbstractSyntaxTreeNode parent = null;
        public List<AbstractSyntaxTreeNode> children = new ArrayList<>();
    }
    
    private static int nofArgs(Tokenizer.Token t)
    {
        String txt = t.text;
        switch(txt)
        {
            // multiplicative
            case "*":
                return 2;
            case "/":
                return 2;
            case "%":
                return 2;

            // additive
            case "+":
                return 2;
            case "-":
                return 2;

            // relational
            case "<":
                return 2;
            case ">":
                return 2;
            case "<=":
                return 2;
            case ">=":
                return 2;

            // equality
            case "==":
                return 2;
            case "!=":
                return 2;

            // logical AND
            case "and":
                return 2;

            // logical OR
            case "or":
                return 2;

            // logical NOT
            case "not":
                return 1;
                
           // text/array
            case "contains":
                return 2;
            case "startswith":
                return 2;
            case "endswith":
                return 2;
            case "size":
                return 1;                
        }
        return 0;
    }
    
    public static AbstractSyntaxTreeNode buildAST(List<Tokenizer.Token> postfix)
    {
        Stack<AbstractSyntaxTreeNode> args = new Stack<>();
        for(int i=0;i<postfix.size();i++)
        {
            Tokenizer.Token t = postfix.get(i);
            
            // operands
            if(t.type != Tokenizer.TokenType.OPERATOR)
            {
                AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode();
                node.token = t;
                args.push(node);
            }
            
            // operators
            else
            {
                AbstractSyntaxTreeNode node = new AbstractSyntaxTreeNode();
                node.token = t;
                for(int j=0;j<nofArgs(t);j++)
                {
                    if(args.isEmpty())
                        throw new IllegalArgumentException("Not enough arguments for function " + t.text + ".");
                    AbstractSyntaxTreeNode child = args.pop();
                    child.parent = node;
                    node.children.add(child);
                }
                args.push(node);
            }
        }
        if(args.size() != 1)
            throw new IllegalArgumentException("Illegal expression");
        return args.pop();
    }
}
