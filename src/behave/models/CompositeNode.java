package behave.models;

import behave.execution.ExecutionContext;
import behave.tools.Log;

import java.util.*;

/**
 * Composite node can contain multiple children
 */
public abstract class CompositeNode implements Node {
    List<Node> m_nodes = new LinkedList<>();
    ListIterator<Node> m_itr;
    Node m_currentNode;

    public void addChild(Node node) {
        if (!m_nodes.contains(node)) {
            m_nodes.add(node);
            //have to invalidate the iterator. basically this should only be called during startup when we create the tree
            m_itr = m_nodes.listIterator();
            m_currentNode = null;
        }
        else {
            throw new RuntimeException("Trying to add the same node twice to CompositeNode");
        }
    }

    @Override
    public void initialize(ExecutionContext context) {
        m_itr = m_nodes.listIterator();
        pickNextNode(context);
    }

    protected void pickNextNode(ExecutionContext context) {
        if (m_itr.hasNext()) {
            m_currentNode = m_itr.next();
            m_currentNode.initialize(context);
        }
        else {
            m_currentNode = null;
        }
    }

    @Override
    public Types.Status tick(ExecutionContext context) {
        if (m_nodes.isEmpty()) {
            Log.warning("CompositeNode has no children!");
            return Types.Status.Success;
        }
        return tickCurrentNode(context);
    }

    protected abstract Types.Status tickCurrentNode(ExecutionContext context);

    protected Types.Status defaultTick(ExecutionContext context, Types.Status endCondition) {
        Types.Status status = m_currentNode.tick(context);
        if (status == Types.Status.Running || status == endCondition) {
            //We're done
            return status;
        }
        else {
            //status depends if we have more nodes to run
            if (m_itr.hasNext()) {
                pickNextNode(context);
                return Types.Status.Running;
            }
            else {
                return status;
            }
        }
    }


    public abstract static class RandomCompositeNode extends CompositeNode {
        @Override
        public void initialize(ExecutionContext context) {
            //Not 100% space efficient since we create a copy of the list, but it's very simple.
            List<Node> shuffledList = new ArrayList<>(m_nodes);
            Collections.shuffle(shuffledList);
            m_itr = shuffledList.listIterator();
            pickNextNode(context);
        }
    }

    /**
     * Will return success if all the nodes success and stop and fail if any of them fails
     */
    public static class SequenceNode extends CompositeNode {
        @Override
        protected Types.Status tickCurrentNode(ExecutionContext context) {
            return defaultTick(context, Types.Status.Failure);
        }
    }

    /**
     * Will return success and quit immediately if any of the nodes succeeded
     */
    public static class SelectorNode extends CompositeNode {
        @Override
        protected Types.Status tickCurrentNode(ExecutionContext context) {
            return defaultTick(context, Types.Status.Success);
        }
    }

    public static class RandomSelectorNode extends RandomCompositeNode {
        @Override
        protected Types.Status tickCurrentNode(ExecutionContext context) {
            return defaultTick(context, Types.Status.Success);
        }
    }
}
