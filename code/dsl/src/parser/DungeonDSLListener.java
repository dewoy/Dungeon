package parser;

import antlr.main.DungeonDSLLexer;
import antlr.main.DungeonDSLParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.AST.EdgeOpNode;
import parser.AST.IdNode;
import parser.AST.Node;
import parser.AST.SourceFileReference;

import java.util.*;

public class DungeonDSLListener implements antlr.main.DungeonDSLListener {

    Stack<parser.AST.Node> astStack;

    public DungeonDSLListener() {
        astStack = new Stack<>();
    }

    public parser.AST.Node Walk(ParseTree parseTree) {
        astStack = new Stack<>();
        ParseTreeWalker.DEFAULT.walk(this, parseTree);
        return astStack.peek();
    }

    @Override
    public void enterProgram(DungeonDSLParser.ProgramContext ctx) {}

    @Override
    public void exitProgram(DungeonDSLParser.ProgramContext ctx) {}

    @Override
    public void enterObj_def(DungeonDSLParser.Obj_defContext ctx) {}

    @Override
    public void exitObj_def(DungeonDSLParser.Obj_defContext ctx) {}

    @Override
    public void enterProperty_def(DungeonDSLParser.Property_defContext ctx) {}

    @Override
    public void exitProperty_def(DungeonDSLParser.Property_defContext ctx) {}

    @Override
    public void enterStmt(DungeonDSLParser.StmtContext ctx) {}

    @Override
    public void exitStmt(DungeonDSLParser.StmtContext ctx) {}

    @Override
    public void enterDot_def(DungeonDSLParser.Dot_defContext ctx) {}

    @Override
    public void exitDot_def(DungeonDSLParser.Dot_defContext ctx) {}

    @Override
    public void enterDot_stmt_list(DungeonDSLParser.Dot_stmt_listContext ctx) {}

    @Override
    public void exitDot_stmt_list(DungeonDSLParser.Dot_stmt_listContext ctx) {}

    @Override
    public void enterDot_stmt(DungeonDSLParser.Dot_stmtContext ctx) {}

    @Override
    public void exitDot_stmt(DungeonDSLParser.Dot_stmtContext ctx) {}

    @Override
    public void enterDot_edge_stmt(DungeonDSLParser.Dot_edge_stmtContext ctx) {}

    @Override
    public void exitDot_edge_stmt(DungeonDSLParser.Dot_edge_stmtContext ctx) {
        var attr_list = Node.NONE;
        if (!ctx.dot_attr_list().isEmpty()) {
            attr_list = astStack.pop();
            assert (attr_list.type == Node.Type.DotAttrList);
        }

        LinkedList<Node> rhsEdges = new LinkedList<>();
        for (int i = 0; i < ctx.dot_edge_RHS().size(); i++) {
            var rhs = astStack.pop();
            assert (rhs.type == Node.Type.DotEdgeRHS);
            rhsEdges.addFirst(rhs);
        }

        var lhsId = astStack.pop();
        assert (lhsId.type == Node.Type.Identifier);

    }

    @Override
    public void enterDot_edge_RHS(DungeonDSLParser.Dot_edge_RHSContext ctx) { }

    @Override
    public void exitDot_edge_RHS(DungeonDSLParser.Dot_edge_RHSContext ctx) {
        // ID will be identifier on stack
        var idNode = astStack.pop();

        // edge_op will be on stack
        var edgeOp = astStack.pop();

        List<Node> children = Arrays.asList(edgeOp, idNode);
        var edgeRhs = new Node(Node.Type.DotEdgeRHS, new ArrayList<>(children));

        astStack.push(edgeRhs);
    }

    @Override
    public void enterDot_attr_stmt(DungeonDSLParser.Dot_attr_stmtContext ctx) {}

    @Override
    public void exitDot_attr_stmt(DungeonDSLParser.Dot_attr_stmtContext ctx) {}

    @Override
    public void enterDot_node_stmt(DungeonDSLParser.Dot_node_stmtContext ctx) {}

    @Override
    public void exitDot_node_stmt(DungeonDSLParser.Dot_node_stmtContext ctx) {}

    @Override
    public void enterDot_attr_list(DungeonDSLParser.Dot_attr_listContext ctx) {}

    @Override
    public void exitDot_attr_list(DungeonDSLParser.Dot_attr_listContext ctx) {}

    @Override
    public void enterDot_a_list(DungeonDSLParser.Dot_a_listContext ctx) {}

    @Override
    public void exitDot_a_list(DungeonDSLParser.Dot_a_listContext ctx) {}

    @Override
    public void enterDot_edge_op(DungeonDSLParser.Dot_edge_opContext ctx) {

    }

    @Override
    public void exitDot_edge_op(DungeonDSLParser.Dot_edge_opContext ctx) {
        var inner = astStack.pop();
        assert (inner.type == Node.Type.Arrow || inner.type == Node.Type.DoubleLine);
        EdgeOpNode.Type edgeOpNodeType = EdgeOpNode.Type.NONE;
        if (inner.type == Node.Type.Arrow) {
            edgeOpNodeType = EdgeOpNode.Type.arrow;
        } else {
            edgeOpNodeType = EdgeOpNode.Type.doubleLine;
        }
        var node = new EdgeOpNode(inner, edgeOpNodeType);
        astStack.push(node);
    }


    private SourceFileReference getSourceFileReference(TerminalNode node) {
        var symbol = node.getSymbol();
        var line = symbol.getLine();
        var column = symbol.getCharPositionInLine();
        return new SourceFileReference(line, column);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        var nodeType = node.getSymbol().getType();
        if (nodeType == DungeonDSLLexer.ID) {
            var idNode = new IdNode(node.getText(), getSourceFileReference(node));
            astStack.push(idNode);
        } else if (nodeType == DungeonDSLLexer.ARROW) {
            var arrowNode = new Node(Node.Type.Arrow, getSourceFileReference(node));
            astStack.push(arrowNode);
        } else if (nodeType == DungeonDSLLexer.DOUBLE_LINE) {
            var doubleLineNode = new Node(Node.Type.DoubleLine, getSourceFileReference(node));
            astStack.push(doubleLineNode);
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {}

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {}

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {}
}
