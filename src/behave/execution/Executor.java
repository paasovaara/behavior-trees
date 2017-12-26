package behave.execution;

import behave.models.Node;
import behave.models.Types;
import behave.tools.Log;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class Executor {
    private Node m_root;
    private ExecutionContext m_context;
    private Timer m_timer;
    private long m_lastTickTimestamp = 0;

    public void initialize(Node root) {
        initialize(root, new ExecutionContext());
    }

    public void initialize(Node root, ExecutionContext context) {
        m_context = context;
        m_root = root;
    }

    public void start(long period, long delay) {
        //In this early phase we use a simple Timer to execute the traversal.
        //This tick could come from external source also, such as some event/game-loop
        //TODO create separate classes, TimerExecutor and ExternalExecutor
        m_lastTickTimestamp = System.currentTimeMillis();

        m_timer = new Timer();
        m_root.initialize(m_context);
        m_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tickNode();
            }
        }, delay, period);
    }

    protected void tickNode() {
        Log.debug("Executor running one Tick");
        long period = System.currentTimeMillis() - m_lastTickTimestamp;

        m_context.setLastTimeStep(period);
        Types.Status status = m_root.tick(m_context);
        if (status != Types.Status.Running) {
            Log.info("Root node has finished with status " + status + ". Ending execution.");
            Log.info(m_context.toString());
            stop();
        }
        m_lastTickTimestamp = System.currentTimeMillis();
    }

    public void stop() {
        m_timer.cancel();
    }
}
