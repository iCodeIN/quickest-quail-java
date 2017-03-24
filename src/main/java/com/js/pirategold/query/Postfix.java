/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.query;

import com.js.pirategold.query.Tokenizer.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author joris
 */
public class Postfix {

    private static int precedence(Tokenizer.Token t) {
        String str = t.text;
        switch (str) {

            // multiplicative
            case "*":
                return 13;
            case "/":
                return 13;
            case "%":
                return 13;

            // additive
            case "+":
                return 12;
            case "-":
                return 12;

            // relational
            case "<":
                return 10;
            case ">":
                return 10;
            case "<=":
                return 10;
            case ">=":
                return 10;

            // equality
            case "==":
                return 9;
            case "!=":
                return 9;

            // logical AND
            case "and":
                return 5;

            // logical OR
            case "or":
                return 4;

            // logical NOT
            case "not":
                return 3;
        }
        return 1024;
    }

    private static boolean isLeftAssociative(Tokenizer.Token t) {
        return true;
    }

    public static List<Tokenizer.Token> toPostfix(List<Tokenizer.Token> infix) {
        Stack<Tokenizer.Token> stk = new Stack<>();
        List<Tokenizer.Token> output = new ArrayList<>();

        for (int i = 0; i < infix.size(); i++) {
            Token t = infix.get(i);

            if(t.type == Tokenizer.TokenType.SPACE)
                continue;
            
            // if the token is a number or literal, or variable, push it to the output queue
            if (t.type == Tokenizer.TokenType.NUMBER || t.type == Tokenizer.TokenType.STRING || t.type == Tokenizer.TokenType.VARIABLE) {
                output.add(t);
                continue;
            }

            // if the token is an operator (o1) then:
            if (t.type == Tokenizer.TokenType.OPERATOR) {
                Token o1 = t;
                while (!stk.isEmpty() && stk.peek().type == Tokenizer.TokenType.OPERATOR
                        && ((isLeftAssociative(o1) && precedence(o1) <= precedence(stk.peek()))
                        || (!isLeftAssociative(o1) && precedence(o1) < precedence(stk.peek())))) {
                    output.add(stk.pop());
                }
                stk.push(o1);
            }

            // if the token is a left parenthesis, push it onto the stack
            if (t.type == Tokenizer.TokenType.PARENTHESIS_LEFT) {
                stk.push(t);
                continue;
            }

            // if the token is a right parenthesis
            if (t.type == Tokenizer.TokenType.PARENTHESIS_RIGHT) {
                while (!stk.isEmpty() && stk.peek().type != Tokenizer.TokenType.PARENTHESIS_LEFT) {
                    output.add(stk.pop());
                }
                if (!stk.isEmpty() && stk.peek().type == Tokenizer.TokenType.PARENTHESIS_LEFT) {
                    stk.pop();
                }
            }
        }
        while(!stk.isEmpty())
        {
            Token t = stk.pop();
            if(t.type == Tokenizer.TokenType.PARENTHESIS_LEFT || t.type == Tokenizer.TokenType.PARENTHESIS_RIGHT)
                throw new IllegalArgumentException("Mismatched parenthesis");
            output.add(t);
        }
        // return
        return output;
    }

}
