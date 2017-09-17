package behave.models;

import behave.execution.ExecutionContext;

public class DecoratorNode implements Node {
    private Node m_child;

    public void addChild(Node node) {
        if (m_child == null) {
            m_child = node;
        }
        else {
            throw new RuntimeException("DecoratorNode can have only one child");
        }
    }

    @Override
    public void initialize(ExecutionContext context) {
        m_child.initialize(context);
    }

    @Override
    public Types.Status tick(ExecutionContext context) {
        return m_child.tick(context);
    }
}
