package behave.models;

import behave.execution.ExecutionContext;
import behave.tools.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ParallelCompositeNode will tick all it's nodes during ticking. Logically this means they are run "in parallel"
 * even though they are executed in the same thread.
 *
 * Be careful of using AsyncNode with ParallelNodes since the async task will not be automatically cancelled even
 * if this node finishes. You will have to handle those cases explicitly.
 *
 * TODO think if ExecutionContext should somehow pass this info to the nodes. Is it necessary?
 * Can it have side-effects if one wants to use timing based on tick-intervals..?
 *
 */
public abstract class ParallelNode implements Node {

    Map<Node, Types.Status> m_statusMap = new HashMap<>();
    List<Node> m_nodes = new LinkedList<>();//This would not necessarily be needed, we could iterate keys.

    @Override
    public void initialize(ExecutionContext context) {
        m_statusMap.clear();
        m_nodes.forEach(node -> {
            m_statusMap.put(node, Types.Status.Running);
            node.initialize(context);
        });

    }

    @Override
    public void addChild(Node node) {
        if (!m_nodes.contains(node)) {
            m_nodes.add(node);
            m_statusMap.put(node, Types.Status.Running);
        }
        else {
            throw new RuntimeException("Trying to add the same node twice to ParallelNode");
        }
    }

    @Override
    public Types.Status tick(ExecutionContext context) {
        if (m_nodes.isEmpty()) {
            Log.warning("ParallelNode has no children!");
            return Types.Status.Success;
        }

        boolean shouldExit = tickAllNodes(context, immediateExitStatus());
        if (shouldExit) {
            return immediateExitStatus();
        }

        boolean stillRunning = isNodeStillRunning();
        if (stillRunning) {
            return Types.Status.Running;
        }
        else {
            //has to be inversion of the immediate since we would've exited by now
            return Types.invert(immediateExitStatus());
        }
    }

    protected boolean isNodeStillRunning() {
        for (Types.Status nodeStatus : m_statusMap.values()) {
            if (nodeStatus == Types.Status.Running) {
                return true;
            }
        }
        return false;
    }

    protected abstract Types.Status immediateExitStatus();

    /**
     * Tick all nodes and update the map
     * @param context
     * @return true if we should exit immediately or false if we can keep on going
     */
    protected boolean tickAllNodes(ExecutionContext context, Types.Status exitCondition) {
        for(Node node: m_nodes) {
            Types.Status lastStatus = m_statusMap.get(node);
            if (lastStatus == Types.Status.Running) {
                Types.Status newStatus = node.tick(context);
                m_statusMap.put(node, newStatus);
                if (newStatus == exitCondition) {
                    //Quit immediately
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ParallelSequenceNode will perform logical AND, thus ticking nodes until first one returns Failed or all of them Succeed.
     *
     * Notice that you can use DecoratorNodes around children to invert/succeed any node you want.
     */
    public static class ParallelSequenceNode extends ParallelNode {
        @Override
        protected Types.Status immediateExitStatus() {
            return Types.Status.Failure;
        }
    }

    /**
     * ParallelSelectorNode will perform logical OR, thus ticking nodes until first one returns Success or all of them end.
     *
     * Notice that you can use DecoratorNodes around children to invert/succeed any node you want.
     */
    public static class ParallelSelectorNode extends ParallelNode {
        @Override
        protected Types.Status immediateExitStatus() {
            return Types.Status.Success;
        }
    }
}
