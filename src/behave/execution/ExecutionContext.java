package behave.execution;

import java.util.HashMap;
import java.util.Map;

public class ExecutionContext {

    private Map<String, Object> m_variables = new HashMap<>();
    private float m_lastTimeStemp = 0.f;

    public void setVariable(String key, Object value) {
        m_variables.put(key, value);
    }

    public Object getVariable(String key) {
        return m_variables.get(key);
    }

    protected void setLastTimeStep(float step) {
        m_lastTimeStemp = step;
    }

    public float getLastTimeStep() {
        return m_lastTimeStemp;
    }
}
