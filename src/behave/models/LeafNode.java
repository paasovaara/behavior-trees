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

    @Override
    public void removeChild(Node node) {}

    @Override
    public void removeChildren() {}

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

    /**
     * AsyncLeafNode will return Running until the Runnable has been finished.
     *
     * TODO implement timeout.
     */
    public abstract static class AsyncLeafNode extends LeafNode {
        protected long m_startTime;
        protected Thread m_thread;
        protected Types.Status m_status = Types.Status.Failure;

        @Override
        public void initialize(ExecutionContext context) {
            super.initialize(context);
            if (m_thread != null && m_thread.isAlive()) {
                Log.warning("AsyncLeafNode is initialised while thread is still running!");
            }
            m_thread = null;
            m_status = Types.Status.Failure;
        }

        @Override
        public Types.Status tick(ExecutionContext context) {
            if (m_thread == null) {
                m_startTime = System.currentTimeMillis();
                m_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            m_status = runBlockingTask();
                        }
                        catch (Exception e) {
                            Log.error("Exception in thread: " + e.getMessage());
                            e.printStackTrace();
                            m_status = Types.Status.Failure;
                        }
                    }
                });
                m_thread.start();
                m_status = Types.Status.Running;
            }
            else if (m_thread.isAlive()) {
                return Types.Status.Running;
            }
            else if (m_status == Types.Status.Running) {
                Log.error("Blocking task returned running, even though the task is finished");
                m_status = Types.Status.Failure;
            }
            return m_status;
        }

        /**
         * This blocking task will be ran inside a thread. return either Success or Failure
         * @return
         */
        protected abstract Types.Status runBlockingTask();
    }

}
