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

    public abstract static class RepeatUntilResultNode extends DecoratorNode {
        protected abstract Types.Status repeatUntilStatus();

        @Override
        public Types.Status tick(ExecutionContext context) {
            Types.Status status = m_child.tick(context);
            if (status == Types.Status.Running) {
                return status;
            }
            else if (status == repeatUntilStatus()) {
                return Types.Status.Success;
            }
            else {
                m_child.initialize(context);
                return Types.Status.Running;
            }
        }
    }

    public static class RepeatUntilFailNode extends RepeatUntilResultNode {
        @Override
        protected Types.Status repeatUntilStatus() {
            return Types.Status.Failure;
        }
    }

    public static class RepeatUntilSuccessNode extends RepeatUntilResultNode {
        @Override
        protected Types.Status repeatUntilStatus() {
            return Types.Status.Success;
        }
    }

    public abstract static class DelayRunningNode extends DecoratorNode {
        protected long m_startTime;
        protected long m_delayMs;

        public DelayRunningNode(long delayInMs) {
            m_delayMs = delayInMs;
        }

        @Override
        public void initialize(ExecutionContext context) {
            super.initialize(context);
            m_startTime = System.currentTimeMillis();
        }
    }

    public static class DelayBeforeRunningNode extends DelayRunningNode {
        public DelayBeforeRunningNode(long delayInMs) {
            super(delayInMs);
        }

        @Override
        public Types.Status tick(ExecutionContext context) {
            long elapsed = System.currentTimeMillis() - m_startTime;
            if (elapsed < m_delayMs) {
                return Types.Status.Running;
            }
            else {
                return m_child.tick(context);
            }
        }
    }


    public static class DelayAfterRunningNode extends DelayRunningNode {
        private boolean m_ranOnce = false;
        private Types.Status m_status = Types.Status.Failure;

        public DelayAfterRunningNode(long delayInMs) {
            super(delayInMs);
        }

        @Override
        public Types.Status tick(ExecutionContext context) {
            if (!m_ranOnce) {
                m_status = m_child.tick(context);
                m_ranOnce = true;
            }

            long elapsed = System.currentTimeMillis() - m_startTime;
            if (elapsed < m_delayMs) {
                return Types.Status.Running;
            }
            else {
                return m_status;
            }
        }
    }

}
