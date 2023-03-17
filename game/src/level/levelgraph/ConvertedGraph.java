package level.levelgraph;

import dslToGame.graph.Graph;
import dslToGame.graph.Node;
import java.util.HashMap;

public record ConvertedGraph(
        Graph<String> graph,
        LevelNode root,
        HashMap<Node<String>, LevelNode> nodeToLevelNode,
        HashMap<LevelNode, Node<String>> levelNodeToNode) {}
