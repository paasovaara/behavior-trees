package test.parallel;

import behave.execution.ExecutionContext;
import behave.models.DecoratorNode;
import behave.models.LeafNode;
import behave.models.Types;
import behave.tools.Log;

public class ParallelNodes {
    public static class CounterNode extends DecoratorNode.FiniteRepeaterNode {

        public CounterNode(boolean success, int times) {
            super(times);
            if (success)
                addChild(new LeafNode.SucceedingLeafNode());
            else
                addChild(new LeafNode.FailingLeafNode());
        }

        @Override
        public Types.Status tick(ExecutionContext context) {
            Types.Status status = super.tick(context);
            Log.debug("Counter value: " + m_counter);
            return status;
        }
    }
}
