package test.async;

import behave.execution.Executor;
import behave.models.*;
import behave.tools.Log;

public class AsyncTest {

    public AsyncTest() {
        Executor exec = new Executor();
        exec.initialize(createTree());
        exec.start(1000, 1000);
    }


    class DelayedTask extends LeafNode.AsyncLeafNode{
        private boolean m_success;
        public DelayedTask(boolean success) {
            super();
            m_success = success;
        }

        @Override
        protected Types.Status runBlockingTask() {
            Log.debug("Starting my work");
            try {
                //Simulate long job with sleep
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.debug("My job is done here");
            return m_success ? Types.Status.Success : Types.Status.Failure;
        }
    }
    private Node createTree() {
        Node root = new CompositeNode.SequenceNode();

        root.addChild(new DelayedTask(true));
        root.addChild(new DelayedTask(true));
        root.addChild(new DelayedTask(false));
        root.addChild(new DelayedTask(true));//This should never execute

        return root;
    }

    public static void main(String[] args) {
        new AsyncTest();
    }
}
