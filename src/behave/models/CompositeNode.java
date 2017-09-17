package behave.models;

import behave.execution.ExecutionContext;

import java.util.LinkedList;
import java.util.List;

/**
 * Composite node can contain multiple children
 */
public abstract class CompositeNode implements Node {
    List<Node> m_nodes = new LinkedList<>();

    public void addChild(Node node) {
        if (!m_nodes.contains(node)) {
            m_nodes.add(node);
        }
    }

    @Override
    public void tick(ExecutionContext context) {
        //TODO this is not very optimized. we could consider looping only nodes that are active, etc.
        m_nodes.forEach(node -> node.tick(context));
    }
}
