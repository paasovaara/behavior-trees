package behave.models;

import behave.execution.ExecutionContext;

public interface Node {
    /**
     * Called before the node starts ticking
     * @param context read/write init parameters to context if needed
     */
    void initialize(ExecutionContext context);

    /**
     * Called periodically
     * @param context
     * @return status. Running keeps the ticks coming to this node, other statuses will cause a new initialization to occur before ticking again.
     */
    Types.Status tick(ExecutionContext context);

    /**
     * Add child to this Node. Note that not all node-types are designed to have children.
     * @param node
     */
    void addChild(Node node);

    /**
     * remove child from this Node
     * @param node
     */
    void removeChild(Node node);

    /**
     * Remove all child Nodes
     */
    void removeChildren();
}
