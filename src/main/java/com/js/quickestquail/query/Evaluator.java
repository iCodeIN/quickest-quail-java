/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.quickestquail.query;

import com.js.quickestquail.query.AbstractSyntaxTree.AbstractSyntaxTreeNode;
import java.util.Map;

/**
 *
 * @author joris
 */
public class Evaluator {

    private static Object modOp(AbstractSyntaxTreeNode root, Map<String, Object> vars) {
        double arg0 = (Double) evaluate(root.children.get(1), vars);
        double arg1 = (Double) evaluate(root.children.get(0), vars);
        int val = (int) arg0 % (int) arg1;
        return new Double(val);
    }

    private static Object containsOp(AbstractSyntaxTreeNode root, Map<String, Object> vars) {
        Object arg0 = evaluate(root.children.get(1), vars);
        Object arg1 = evaluate(root.children.get(0), vars);
        if (arg0 instanceof String && arg1 instanceof String) {
            String s0 = (String) arg0;
            String s1 = (String) arg1;
            return s0.toUpperCase().contains(s1.toUpperCase());
        }
        if (arg0 instanceof String[] && arg1 instanceof String) {
            for (String t : (String[]) arg0) {
                if (t.equalsIgnoreCase(arg1.toString())) {
                    return true;
                }
            }
            return false;
        }
        throw new IllegalArgumentException("Invalid argument types for CONTAINS");
    }

    private static Object startsWithOp(AbstractSyntaxTreeNode root, Map<String, Object> vars) {
        Object arg0 = evaluate(root.children.get(1), vars);
        Object arg1 = evaluate(root.children.get(0), vars);
        if (arg0 instanceof String && arg1 instanceof String) {
            return ((String) arg0).startsWith((String) arg1);
        }
        if (arg0 instanceof String[] && arg1 instanceof String) {
            String[] arr = (String[]) arg0;
            return arr.length > 0 && arr[0].equalsIgnoreCase(arg1.toString());
        }
        throw new IllegalArgumentException("Invalid argument types for STARTSWITH");
    }

    private static Object endsWithOp(AbstractSyntaxTreeNode root, Map<String, Object> vars) {
        Object arg0 = evaluate(root.children.get(1), vars);
        Object arg1 = evaluate(root.children.get(0), vars);
        if (arg0 instanceof String && arg1 instanceof String) {
            return ((String) arg0).endsWith((String) arg1);
        }
        if (arg0 instanceof String[] && arg1 instanceof String) {
            String[] arr = (String[]) arg0;
            return arr.length > 0 && arr[arr.length - 1].equalsIgnoreCase(arg1.toString());
        }
        throw new IllegalArgumentException("Invalid argument types for ENDSWITH");
    }

    private static Object sizeOp(AbstractSyntaxTreeNode root, Map<String, Object> vars) {
        Object arg0 = evaluate(root.children.get(0), vars);
        if(arg0 instanceof String)
        {
            return ((String) arg0).length() + 0.0;
        }
        if(arg0 instanceof String[])
        {
            return ((String[]) ((String[]) arg0)).length + 0.0;
        }
        throw new IllegalArgumentException("Invalid argument types for SIZE");        
    }

    public static Object evaluate(AbstractSyntaxTreeNode root, Map<String, Object> vars) {
        String txt = root.token.text;
        switch (txt) {
            // multiplicative
            case "*":                
                return (Double) evaluate(root.children.get(1), vars) * (Double) evaluate(root.children.get(0), vars);
            case "/":
                return (Double) evaluate(root.children.get(1), vars) / (Double) evaluate(root.children.get(0), vars);
            case "%":
                return modOp(root, vars);
            // additive
            case "+":
                return (Double) evaluate(root.children.get(1), vars) + (Double) evaluate(root.children.get(0), vars);
            case "-":
                return (Double) evaluate(root.children.get(1), vars) - (Double) evaluate(root.children.get(0), vars);

            // relational
            case "<":
                return (Double) evaluate(root.children.get(1), vars) < (Double) evaluate(root.children.get(0), vars);
            case ">":
                return (Double) evaluate(root.children.get(1), vars) > (Double) evaluate(root.children.get(0), vars);
            case "<=":
                return (Double) evaluate(root.children.get(1), vars) <= (Double) evaluate(root.children.get(0), vars);
            case ">=":
                return (Double) evaluate(root.children.get(1), vars) >= (Double) evaluate(root.children.get(0), vars);

            // equality
            case "==":
                return evaluate(root.children.get(1), vars).equals(evaluate(root.children.get(0), vars));
            case "!=":
                return !(evaluate(root.children.get(1), vars).equals(evaluate(root.children.get(0), vars)));

            // logical AND
            case "and":
                return (Boolean) evaluate(root.children.get(1), vars) && (Boolean) evaluate(root.children.get(0), vars);

            // logical OR
            case "or":
                return (Boolean) evaluate(root.children.get(1), vars) || (Boolean) evaluate(root.children.get(0), vars);

            // logical NOT
            case "not":
                return !(Boolean) evaluate(root.children.get(1), vars);

            // contains
            case "contains":
                return containsOp(root, vars);

            // startsWith
            case "startsWith":
                return startsWithOp(root, vars);

            // endsWith
            case "endsWith":
                return endsWithOp(root, vars);

            // size
            case "size":
                return sizeOp(root, vars);

        }
        if (root.token.type == Tokenizer.TokenType.STRING) {
            return root.token.text.substring(1, root.token.text.length() - 1);
        }
        if (root.token.type == Tokenizer.TokenType.NUMBER) {
            return Double.parseDouble(root.token.text);
        }
        if (root.token.type == Tokenizer.TokenType.VARIABLE) {
            Object obj = vars.get(root.token.text);
            if (obj == null) {
                throw new IllegalArgumentException("Unknown variable " + root.token.text + ".");
            }
            if (obj instanceof Double) {
                return obj;
            }
            if (obj instanceof Integer) {
                return ((Integer) obj).doubleValue();
            }
            if (obj instanceof Float) {
                return ((Float) obj).doubleValue();
            }
            return vars.get(root.token.text);
        }
        return null;
    }
}
