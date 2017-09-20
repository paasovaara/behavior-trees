package test.parallel;

import behave.execution.Executor;
import behave.models.CompositeNode;
import behave.models.DecoratorNode;
import behave.models.Node;
import behave.models.ParallelNode;

public class ParallelTest {

    public ParallelTest() {
        Executor exec = new Executor();
        exec.initialize(createTree());
        exec.start(1000, 1000);
    }

    private Node createTree() {
        Node root = new CompositeNode.SequenceNode();
        Node parallelSeq = new ParallelNode.ParallelSequenceNode();
        root.addChild(parallelSeq);
        parallelSeq.addChild(new ParallelNodes.CounterNode(true, 1));
        parallelSeq.addChild(new ParallelNodes.CounterNode(true, 2));
        parallelSeq.addChild(new ParallelNodes.CounterNode(true, 3));

        Node inverter = new DecoratorNode.InverterNode(); //Invert so selectors will run also
        root.addChild(inverter);
        Node parallelSeq2 = new ParallelNode.ParallelSequenceNode();
        inverter.addChild(parallelSeq2);
        parallelSeq2.addChild(new ParallelNodes.CounterNode(false, 2));
        parallelSeq2.addChild(new ParallelNodes.CounterNode(true, 2));//should execute only once
        parallelSeq2.addChild(new ParallelNodes.CounterNode(true, 2));//should execute only once

        Node parallelSelector = new ParallelNode.ParallelSelectorNode();
        root.addChild(parallelSelector);
        parallelSelector.addChild(new ParallelNodes.CounterNode(true, 2));
        parallelSelector.addChild(new ParallelNodes.CounterNode(true, 2));//should execute only once
        parallelSelector.addChild(new ParallelNodes.CounterNode(true, 2));//should execute only once

        Node parallelSelector2 = new ParallelNode.ParallelSelectorNode();
        root.addChild(parallelSelector2);
        parallelSelector2.addChild(new ParallelNodes.CounterNode(false, 1));
        parallelSelector2.addChild(new ParallelNodes.CounterNode(false, 2));
        parallelSelector2.addChild(new ParallelNodes.CounterNode(true, 3));

        return root;
    }

    public static void main(String[] args) {
        new ParallelTest();
    }
}
