package test.robot;

import behave.execution.Executor;
import behave.models.*;

public class RobotTest {

    public RobotTest() {
        Executor exec = new Executor();
        exec.initialize(createTree());
        exec.start(1000, 1000);
    }

    private Node createTree() {
        Node root = new CompositeNode.SequenceNode();
        //Go to initial position
        root.addChild(new RobotNodes.RobotMoveNode());

        //say something
        root.addChild(new RobotNodes.RobotSpeakNode("Hear me out, I'm the mighty Robot!"));

        //Then keep recognizing persons until we find someone
        Node repeater = new DecoratorNode.RepeatUntilSuccessNode();
        repeater.addChild(new RobotNodes.RobotLookForInteraction());
        root.addChild(repeater);

        return root;
    }

    public static void main(String[] args) {
        new RobotTest();
    }

}
