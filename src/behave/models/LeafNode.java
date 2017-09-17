package behave.models;

import behave.execution.ExecutionContext;

/**
 * This is the default LeafNode implementation which does nothing.
 * Create some actual logic by extending this class and use other nodes to create the tree.
 */
public class LeafNode implements Node {
    @Override
    public void initialize(ExecutionContext context) {

    }

    @Override
    public Types.Status tick(ExecutionContext context) {
        return Types.Status.Success;
    }

    @Override
    public void addChild(Node node) {
        throw new RuntimeException("Leaf node cannot have children");
    }


}
