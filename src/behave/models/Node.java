package behave.models;

import behave.execution.ExecutionContext;

public interface Node {
    void tick(ExecutionContext context);
}
