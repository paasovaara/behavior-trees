package behave.models;

import behave.execution.ExecutionContext;

public abstract class DecoratorNode implements Node {
    protected Node m_child;

    public void addChild(Node node) {
        if (m_child == null) {
            m_child = node;
        }
        else {
            throw new RuntimeException("DecoratorNode can have only one child");
        }
    }

    @Override
    public void initialize(ExecutionContext context) {
        m_child.initialize(context);
    }

    public static class InverterNode extends DecoratorNode {
        @Override
        public Types.Status tick(ExecutionContext context) {
            Types.Status status = m_child.tick(context);
            if (status == Types.Status.Failure) {
                return Types.Status.Success;
            }
            else if (status == Types.Status.Success) {
                return Types.Status.Failure;
            }
            else {
                return status;
            }
        }
    }

    public static class SucceederNode extends DecoratorNode {
        @Override
        public Types.Status tick(ExecutionContext context) {
            Types.Status status = m_child.tick(context);
            if (status == Types.Status.Running) {
                return Types.Status.Running;
            }
            else return Types.Status.Success;
        }
    }

    public static class InfiniteRepeaterNode extends DecoratorNode {
        @Override
        public Types.Status tick(ExecutionContext context) {
            Types.Status status = m_child.tick(context);
            if (status != Types.Status.Running) {
                m_child.initialize(context);
            }
            return Types.Status.Running;
        }
    }

    public static class FiniteRepeaterNode extends DecoratorNode {
        int m_times = 0;
        int m_counter = 0;

        public FiniteRepeaterNode(int times) {
            m_times = times;
            m_counter = times;
            if (times <= 0) {
                throw new RuntimeException("FiniteRepeaterNode times has to be positive");
            }
        }

        @Override
        public void initialize(ExecutionContext context) {
            super.initialize(context);
            m_counter = m_times;
        }

        @Override
        public Types.Status tick(ExecutionContext context) {
            Types.Status status = m_child.tick(context);
            if (status == Types.Status.Running) {
                return Types.Status.Running;
            }
            else {
                m_counter--;
                if (m_counter == 0) {
                    return status;
                }
                else {
                    m_child.initialize(context);
                    return Types.Status.Running;
                }
            }
        }
    }

    public static class RepeateUntilFailNode extends DecoratorNode {
        @Override
        public Types.Status tick(ExecutionContext context) {
            Types.Status status = m_child.tick(context);
            if (status == Types.Status.Failure) {
                return Types.Status.Success;
            }
            else {
                m_child.initialize(context);
                return Types.Status.Running;
            }
        }
    }


}
