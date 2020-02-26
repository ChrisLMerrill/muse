package org.museautomation.builtins.plugins.input;

import org.museautomation.core.*;
import org.museautomation.core.task.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleFixedInputProvider implements InputProvider
    {
    @Override
    public boolean isLastChanceProvider()
        {
        return false;
        }

    @Override
    public Map<String, Object> gatherInputValues(TaskInputSet inputs, MuseExecutionContext context)
        {
        Map<String, Object> result = new HashMap<>();
        for (String name : inputs.getInputNames())
            if (_values.containsKey(name))
                result.put(name, _values.get(name));  // TODO check types
        return result;
        }

    public void put(String name, Object value)
        {
        _values.put(name, value);
        }

    private Map<String, Object> _values = new HashMap<>();
    }