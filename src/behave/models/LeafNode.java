package behave.models;

import behave.execution.ExecutionContext;
import behave.tools.Log;

/**
 * This is the default LeafNode implementation which does nothing.
 * Create some actual logic by extending this class and use other nodes to create the tree.
 */
public abstract class LeafNode implements Node {
    @Override
    public void initialize(ExecutionContext context) {}

    @Override
    public void addChild(Node node) {
        throw new RuntimeException("Leaf node cannot have children");
    }

    /**
     * These two nodes are just for debugging. Always implement your own.
     */

    public static class SucceedingLeafNode extends LeafNode {
        @Override
        public Types.Status tick(ExecutionContext context) {
            Log.info("I will always succeed");
            return Types.Status.Success;
        }
    }

    public static class FailingLeafNode extends LeafNode {
        @Override
        public Types.Status tick(ExecutionContext context) {
            Log.info("I will always fail");

            return Types.Status.Failure;
        }
    }

}
