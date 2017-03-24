/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.js.pirategold.query;

import com.js.pirategold.query.AbstractSyntaxTree.AbstractSyntaxTreeNode;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

/**
 *
 * @author joris
 */
public class ASTVisualizer {
    
    public static Graph buildGraph(AbstractSyntaxTreeNode root)
    {
        Graph g = new MultiGraph("AST");

        g.addNode("node_0");
        g.getNode("node_0").setAttribute("ui.label", root.token.text + " [ROOT]");
            
        buildGraph(root, "node_0", g);
        return g;
    }
    
    private static void buildGraph(AbstractSyntaxTreeNode root, String rootId, Graph g)
    {
        for(AbstractSyntaxTreeNode child : root.children)
        {
            String nextId = "node_" + g.getNodeCount();            
            g.addNode(nextId);
            g.getNode(nextId).setAttribute("ui.label", child.token.text);
            g.addEdge("edge_" + g.getEdgeCount(), rootId, nextId);
            // recurse
            buildGraph(child, nextId, g);
        }
    }
}
