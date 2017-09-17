package behave.models;

import behave.execution.ExecutionContext;

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
