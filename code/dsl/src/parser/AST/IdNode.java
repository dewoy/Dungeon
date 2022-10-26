package parser.AST;

public class IdNode extends Node {
    private final String name;

    public IdNode(String name, SourceFileReference sourceFileReference) {
        super(Type.Identifier, sourceFileReference);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T Accept(AstVisitor<T> visitor) {
        return visitor.Visit(this);
    }
}
