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
        m_timer = new Timer();
        m_root.initialize(m_context);
        m_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //let's assume timer is deterministic, but TODO measure the step using System.currentTimeInMillis
                Log.debug("Executor running one Tick");
                m_context.setLastTimeStep(period);
                Types.Status status = m_root.tick(m_context);
                if (status != Types.Status.Running) {
                    Log.info("Root node has finished with status " + status + ". Ending execution.");
                    stop();
                }
            }
        }, delay, period);
    }

    public void stop() {
        m_timer.cancel();
    }
}
