package behave.models;

import behave.execution.ExecutionContext;
import behave.tools.Log;

import java.util.Random;

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
            Log.debug("I will always succeed");
            return Types.Status.Success;
        }
    }

    public static class FailingLeafNode extends LeafNode {
        @Override
        public Types.Status tick(ExecutionContext context) {
            Log.debug("I will always fail");

            return Types.Status.Failure;
        }
    }

    public static class RandomLeafNode extends LeafNode {
        float[] m_odds = new float[3];
        float m_sum = 0.f;
        Random m_random = new Random();

        public RandomLeafNode(float successOdds, float failureOdds, float runningOdds) {
            m_sum = runningOdds + successOdds + failureOdds;
            m_odds[0] = successOdds;
            m_odds[1] = failureOdds;
            m_odds[2] = runningOdds;
        }

        @Override
        public Types.Status tick(ExecutionContext context) {
            Types.Status status;
            float rand = m_sum * m_random.nextFloat();
            if (rand <= m_odds[0]) {
                status = Types.Status.Success;
            }
            else if (rand <= m_odds[0] + m_odds[1]) {
                status = Types.Status.Failure;
            }
            else {
                status = Types.Status.Running;
            }

            Log.debug(String.format("This time I happened to %s with odds (%f)", status.toString(), rand));
            return status;
        }
    }

}
