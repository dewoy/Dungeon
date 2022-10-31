package interpreter.dot;

import java.util.Dictionary;
import java.util.Hashtable;

import graph.EdgeDirection;
import parser.AST.*;

public class Interpreter implements AstVisitor<Object> {
    // how to build graph?
    // - need nodes -> hashset, quasi symboltable
    //Dictionary<String, GraphNode> graphNodes = new Hashtable<>();
    Dictionary<String, graph.Node<String>> graphNodes = new Hashtable<>();
    Dictionary<graph.Node<String>, Integer> totalChildConnections = new Hashtable<>();
    Dictionary<graph.Node<String>, Integer> connectedChildren = new Hashtable<>();

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

            totalChildConnections.put(graphNode, 0);
            connectedChildren.put(graphNode, 0);
        }

        // return Dot-Node
        return graphNodes.get(name);
    }

    @Override
    public Object visit(BinaryNode node) {
        return null;
    }

    private void connectNodes(DotDefNode node) {
        for (parser.AST.Node edgeStmt : node.getStmtNodes()) {
            var edgeStmtNode = (EdgeStmtNode)edgeStmt;
            graph.Node<String> lhsDotNode = (graph.Node<String>) edgeStmtNode.getLhsId().accept(this);
            graph.Node<String> rhsDotNode = null;

            for (Node edge : edgeStmtNode.getRhsStmts()) {
                assert (edge.type.equals(Node.Type.DotEdgeRHS));

                EdgeRhsNode edgeRhs = (EdgeRhsNode) edge;
                rhsDotNode = (graph.Node<String>) edgeRhs.getIdNode().accept(this);

                int totalConnections = totalChildConnections.get(lhsDotNode);
                int currentConnections = connectedChildren.get(lhsDotNode);

                try {
                    var edgeDirection = mapChildIdxToEdgeDirection(totalConnections, currentConnections);
                    boolean didConnect = lhsDotNode.connect(rhsDotNode, edgeDirection);
                    if (didConnect) {
                        connectedChildren.put(lhsDotNode, ++currentConnections);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                lhsDotNode = rhsDotNode;
            }
        }
    }

    private String getEdgeName(graph.Node<String> node, EdgeDirection direction) {
        var neighbor = node.getNeighbour(direction);
        assert (neighbor.hasValue());

        return "NODE["+neighbor.getValue()+"] -"+direction+" OF- NODE["+node.getValue()+"]";
    }

    @Override
    public Object visit(DotDefNode node) {
        this.graphEdges = new Hashtable<>();
        this.graphNodes = new Hashtable<>();
        this.totalChildConnections = new Hashtable<>();
        this.connectedChildren = new Hashtable<>();

        String name = node.getGraphId();

        // 1. store info in internal structure (in visitor-implementation)
        // 2. convert internal structure (with all information) into external graph (in connectNodes)

        for (parser.AST.Node edgeStmt : node.getStmtNodes()) {
            edgeStmt.accept(this);
        }

        connectNodes(node);

        var nodeIter = graphNodes.elements().asIterator();
        while (nodeIter.hasNext()) {
            var graphNode = nodeIter.next();
            for (var direction : graph.EdgeDirection.values()) {
                if (graphNode.hasNeighbourInDirection(direction)) {
                    System.out.println("Edge: [" + getEdgeName(graphNode, direction) + "]");
                }
            }
        }

        return null;
    }

    @Override
    public Object visit(EdgeRhsNode node) {
        return null;
    }

    private EdgeDirection mapChildIdxToEdgeDirection(int totalChildCount, int indexOfCurrentChild) throws Exception {
        return switch (totalChildCount) {
            case 0 -> throw new Exception("TotalChildCount is 0");
            case 1 -> EdgeDirection.DOWN;
            case 2 -> switch (indexOfCurrentChild) {
                case 0 -> EdgeDirection.LEFT;
                case 1 -> EdgeDirection.RIGHT;
                default -> throw new Exception("Too many children");
            };
            case 3 -> switch (indexOfCurrentChild) {
                case 0 -> EdgeDirection.LEFT;
                case 1 -> EdgeDirection.DOWN;
                case 2 -> EdgeDirection.RIGHT;
                default -> throw new Exception("Too many children");
            };
            default -> throw new Exception("Invalid value for totalChildCount");
        };
    }

    @Override
    public Object visit(EdgeStmtNode node) {
        // TODO: add handling of edge-attributes

        // node will contain all edge definitions
        graph.Node<String> lhsDotNode = (graph.Node<String>) node.getLhsId().accept(this);
        graph.Node<String> rhsDotNode = null;

        for (Node edge : node.getRhsStmts()) {
            assert (edge.type.equals(Node.Type.DotEdgeRHS));

            EdgeRhsNode edgeRhs = (EdgeRhsNode) edge;
            rhsDotNode = (graph.Node<String>) edgeRhs.getIdNode().accept(this);

            GraphEdge.Type edgeType =
                edgeRhs.getEdgeOpType().equals(EdgeOpNode.Type.arrow)
                ? GraphEdge.Type.directed
                : GraphEdge.Type.undirected;

            int connections = totalChildConnections.get(lhsDotNode);
            totalChildConnections.put(lhsDotNode, ++connections);

            lhsDotNode = rhsDotNode;
        }


        return null;
    }

    @Override
    public Object visit(EdgeOpNode node) {
        return null;
    }
}
