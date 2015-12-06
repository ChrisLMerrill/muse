package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class VariableValueSourceStringExpressionSupport implements ValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
        {
        if (string.startsWith("$"))
            {
            List<ValueSourceConfiguration> configurations = ValueSourceQuickEditSupporters.parseWithAll(string.substring(1), project);
            if (configurations.size() > 0)
                {
                ValueSourceConfiguration config = new ValueSourceConfiguration();
                config.setType(VariableValueSource.TYPE_ID);
                config.setSource(configurations.get(0));
                return config;
                }
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(VariableValueSource.TYPE_ID))
            {
            if (config.getValue() != null)
                return "$\"" + config.getValue().toString() + "\"";
            else
                {
                List<String> strings = ValueSourceQuickEditSupporters.asStringFromAll(config.getSource(), project);
                if (strings.size() > 0)
                    return "$" + strings.get(0);
                }
            }
        return null;
        }

    @Override
    public int getPriority()
        {
        return 2;
        }
    }


