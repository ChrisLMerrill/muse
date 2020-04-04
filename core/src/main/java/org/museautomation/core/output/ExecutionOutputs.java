package org.museautomation.core.output;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ExecutionOutputs
    {
    public void storeOutput(String name, Object value)
        {
        _outputs.put(name, value);
        }

    public Object getOutput(String name)
        {
        return _outputs.get(name);
        }

    public Set<String> getOutputNames()
        {
        return _outputs.keySet();
        }

    private Map<String, Object> _outputs = new HashMap<>();
    }