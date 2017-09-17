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
     * @return status. Running keeps the ticks coming, other statuses won't
     */
    Types.Status tick(ExecutionContext context);

    void addChild(Node node);

}
