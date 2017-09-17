package test.robot;

import behave.execution.ExecutionContext;
import behave.models.*;
import behave.tools.Log;

public class RobotNodes {

    //We'll simulate robot's movements with delay
    public static class RobotMoveNode extends DecoratorNode.DelayBeforeRunningNode {
        public RobotMoveNode() {
            super(3000);
            Node leaf = new LeafNode() {
                @Override
                public Types.Status tick(ExecutionContext context) {
                    Log.info("End position reached");
                    return Types.Status.Success;
                }
            };
            addChild(leaf);
        }

        @Override
        public Types.Status tick(ExecutionContext context) {
            Log.info("Moving servo motors to place hand into position (X,Y,Z)....");
            return super.tick(context);
        }
    }

    //We'll simulate speaking by starting TTS and then just waiting for it to stop
    public static class RobotSpeakNode extends DecoratorNode.DelayAfterRunningNode {
        public RobotSpeakNode(String msg) {
            super(3000);
            Node leaf = new LeafNode() {
                @Override
                public Types.Status tick(ExecutionContext context) {
                    Log.info(msg);
                    return Types.Status.Success;
                }
            };
            addChild(leaf);
        }
    }

    //////////////////////////////////////////////////////////////////////
    // Try to look something to do from some interactions.
    public static class RobotLookForInteraction extends CompositeNode.SelectorNode {
        public RobotLookForInteraction() {
            super();
            addChild(new MaybeGreetPerson());
            Node fail = new DecoratorNode.FailureNode();
            //This speak is just an action, it's never a success for finding interaction
            fail.addChild(new RobotSpeakNode("So sad since I don't know anyone.."));
            addChild(fail);

        }
    }


    public static class MaybeGreetPerson extends CompositeNode.SequenceNode {
        public MaybeGreetPerson() {
            addChild(new MaybeFindFace());
            addChild(new MaybeRecognizeFace());
            addChild(new RobotSpeakNode("So nice to meet you in person"));
        }
    }

    public static class MaybeFindFace extends LeafNode.RandomLeafNode {
        public MaybeFindFace() {
            super(0.6f, 0.4f, 0.0f);
        }

        @Override
        public Types.Status tick(ExecutionContext context) {
            Log.info("Looking for faces...");
            Types.Status status = super.tick(context);
            Log.info("Found a face: " + status.toString());
            return status;
        }
    }

    public static class MaybeRecognizeFace extends LeafNode.RandomLeafNode {
        public MaybeRecognizeFace() {
            super(0.5f, 0.5f, 0.0f);
        }

        @Override
        public Types.Status tick(ExecutionContext context) {
            Log.info("Recognizing Face...");
            Types.Status status = super.tick(context);
            Log.info("face recognized: " + status.toString());
            return status;
        }
    }

}
