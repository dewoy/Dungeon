package graph;

public class Node<T extends Object> {
    private T value;
    private Node[] neighbours;

    public Node(T value) {
        this.value = value;
        neighbours = new Node[4];
    }

    public boolean connect(Node other, EdgeDirection direction, boolean skipLoop) {
        if (neighbours[direction.value] == null) {
            if (skipLoop || other.connect(this, EdgeDirection.getOpposite(direction), true)) {
                neighbours[direction.value] = other;
                return true;
            }
        }
        return false;
    }

    public boolean connect(Node other, EdgeDirection direction) {
        return connect(other, direction, false);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean hasValue() {
        return this.value != null;
    }

    public Node getNeighbour(EdgeDirection direction) {
        return neighbours[direction.value];
    }

    public boolean hasNeighbourInDirection(EdgeDirection direction) {
        var neighbour = neighbours[direction.value];
        return neighbour != null && neighbour.hasValue();
    }

    public Node[] getNeighbours() {
        return neighbours;
    }

    public T getValue() {
        return value;
    }
}
