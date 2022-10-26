package parser.AST;

public interface AstVisitor<T> {
    T Visit(Node node);
    T Visit(IdNode node);

    T Visit(BinaryNode node);
}
