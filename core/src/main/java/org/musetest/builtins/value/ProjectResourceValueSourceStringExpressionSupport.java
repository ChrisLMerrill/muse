package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ProjectResourceValueSourceStringExpressionSupport implements ValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
        {
        if (string.startsWith("#"))
            {
            ValueSourceConfiguration config = new ValueSourceConfiguration();
            config.setType(ProjectResourceValueSource.TYPE_ID);
            List<ValueSourceConfiguration> configurations = ValueSourceQuickEditSupporters.parseWithAll(string.substring(1), project);
            if (configurations.size() > 0)
                {
                config.setSource(configurations.get(0));
                return config;
                }
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(ProjectResourceValueSource.TYPE_ID))
            {
            ValueSourceConfiguration id_source = config.getSource();
            if (config.getValue() != null)
                return "#\"" + config.getValue().toString() + "\"";
            else
                {
                List<String> strings = ValueSourceQuickEditSupporters.asStringFromAll(config.getSource(), project);
                if (strings.size() > 0)
                    return "#" + strings.get(0);
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


