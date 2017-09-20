package test.riddler;

import behave.execution.ExecutionContext;
import behave.execution.Executor;
import behave.models.*;
import behave.tools.Log;

public class RiddlerTest {

    public RiddlerTest() {
        Executor exec = new Executor();
        exec.initialize(createTree());
        exec.start(1000, 1000);
    }

    private Node createTree() {
        Node root = new DecoratorNode.FiniteRepeaterNode(5);

        Node randomGuesser = new CompositeNode.RandomSelectorNode();
        root.addChild(randomGuesser);

        //One out of three wins, we guess in random order
        randomGuesser.addChild(new LeafNode.FailingLeafNode());
        randomGuesser.addChild(new LeafNode.FailingLeafNode());
        randomGuesser.addChild(new LeafNode.SucceedingLeafNode(){
            @Override
            public Types.Status tick(ExecutionContext context) {
                Log.info("I won, I won!!!");
                return super.tick(context);
            }
        });

        return root;
    }

    public static void main(String[] args) {
        new RiddlerTest();
    }
}
