package interpreter.dot;

import java.util.Dictionary;
import java.util.Hashtable;
import parser.AST.*;

public class Interpreter implements AstVisitor<Object> {
    // how to build graph?
    // - need nodes -> hashset, quasi symboltable
    //Dictionary<String, GraphNode> graphNodes = new Hashtable<>();
    Dictionary<String, graph.Node<String>> graphNodes = new Hashtable<>();
    Dictionary<graph.Node<String>, Integer> childConnections = new Hashtable<>();

    // - need edges (between two nodes)
    //      -> hashset with string-concat of Names with edge_op as key
    Dictionary<String, GraphEdge> graphEdges = new Hashtable<>();

    @Override
    public Object visit(parser.AST.Node node) {
        // traverse down..
        for (parser.AST.Node child : node.getChildren()) {
            child.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(IdNode node) {
        String name = node.getName();
        // lookup and create, if not present previously
        if (graphNodes.get(name) == null) {
            var graphNode = new graph.Node<>(name);
            graphNodes.put(name, graphNode);
            //graphNodes.put(name, new GraphNode(name));
        }

        // return Dot-Node
        return graphNodes.get(name);
    }

    @Override
    public Object visit(BinaryNode node) {
        return null;
    }

    // TODO:
    // - add calls to graph.Node.connect
    @Override
    public Object visit(DotDefNode node) {
        this.graphEdges = new Hashtable<>();
        this.graphNodes = new Hashtable<>();

        String name = node.getGraphId();

        for (parser.AST.Node edgeStmt : node.getStmtNodes()) {
            edgeStmt.accept(this);
        }

        // TODO: cleanup and package in graph class
        // for testing
        System.out.println("parsed graph [" + name + "]");
        var edgeIter = graphEdges.elements().asIterator();
        while (edgeIter.hasNext()) {
            var edge = edgeIter.next();
            System.out.println("Edge: [" + edge.getName() + "]");
        }

        return null;
    }

    @Override
    public Object visit(EdgeRhsNode node) {
        return null;
    }

    @Override
    public Object visit(EdgeStmtNode node) {
        // TODO: add handling of edge-attributes



        return null;
    }

    @Override
    public Object visit(EdgeOpNode node) {
        return null;
    }
}
