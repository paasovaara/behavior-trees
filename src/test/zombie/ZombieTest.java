package test.zombie;

import behave.execution.ExecutionContext;
import behave.execution.Executor;
import behave.models.*;

public class ZombieTest {

    public ZombieTest() {
        Executor exec = new Executor();
        exec.initialize(createTree());
        exec.start(1000, 1000);
    }

    private Node createTree() {
        Node root = new DecoratorNode.FiniteRepeaterNode(5);

        Node seq = new CompositeNode.SelectorNode();
        root.addChild(seq);

        seq.addChild(new LeafNode.FailingLeafNode());
        seq.addChild(new LeafNode.FailingLeafNode());
        seq.addChild(new LeafNode.SucceedingLeafNode());

        return root;
    }

    public static void main(String[] args) {
        new ZombieTest();
    }
}
