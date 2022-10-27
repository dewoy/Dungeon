package parser.AST;

public interface AstVisitor<T> {
    T Visit(Node node);

    T Visit(IdNode node);

    T Visit(BinaryNode node);

    T Visit(DotDefNode node);

    T Visit(EdgeRhsNode node);

    T Visit(EdgeStmtNode node);

    T Visit(EdgeOpNode node);
}
