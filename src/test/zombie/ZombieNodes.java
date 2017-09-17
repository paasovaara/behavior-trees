package test.zombie;

import behave.execution.ExecutionContext;
import behave.models.*;
import behave.tools.Log;

public class ZombieNodes {
    public static class HuntFromHouse extends CompositeNode.SelectorNode {
        public HuntFromHouse() {
            //I will hunt from this house until I succeed.
            Node root = new DecoratorNode.RepeatUntilSuccessNode();
            addChild(root);

            //I will search these rooms in order, sometimes I find, sometimes I don't
            Node selector = new CompositeNode.SelectorNode();
            root.addChild(selector);
            selector.addChild(new HuntFromBathroom());
            selector.addChild(new HuntFromLivingRoom());
        }
    }

    public static class HuntFromBathroom extends LeafNode.RandomLeafNode {
        public HuntFromBathroom() {
            super(0.1f, 0.9f, 0.0f);
        }
        @Override
        public Types.Status tick(ExecutionContext context) {
            Types.Status status = super.tick(context);

            Log.debug("is there humans in the bathroom..? " + status);
            return status;
        }
    }

    public static class HuntFromLivingRoom extends LeafNode.RandomLeafNode {
        public HuntFromLivingRoom() {
            //Larger room so it needs more looking
            super(0.2f, 0.2f, 0.6f);
        }

        @Override
        public Types.Status tick(ExecutionContext context) {
            Types.Status status = super.tick(context);

            Log.debug("is there humans in the Living room..? " + status);
            return status;
        }
    }

}
