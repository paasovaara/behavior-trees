package behave.models;

public abstract class DecoratorNode implements Node {
    private Node m_child;
    public void addChild(Node node) {
        if (m_child == null) {
            m_child = node;
        }
        else {
            throw new RuntimeException("DecoratorNode can have only one child");
        }
    }
}
