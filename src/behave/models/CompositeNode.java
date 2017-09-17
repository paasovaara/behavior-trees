package behave.models;

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
}
