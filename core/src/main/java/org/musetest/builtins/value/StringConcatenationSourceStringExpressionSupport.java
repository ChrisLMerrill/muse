package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class StringConcatenationSourceStringExpressionSupport implements ValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration parse(String string, MuseProject project)
        {
        if (string.contains("+"))
            {
            ValueSourceConfiguration config = new ValueSourceConfiguration();
            config.setType(StringConcatenationSource.TYPE_ID);

            int index = 0;
            StringTokenizer tokenizer = new StringTokenizer(string, "+");
            while (tokenizer.hasMoreTokens())
                {
                List<ValueSourceConfiguration> configurations = ValueSourceQuickEditSupporters.parseWithAll(tokenizer.nextToken().trim(), project);
                if (configurations.size() == 0)
                    return null;

                ValueSourceConfiguration source = configurations.get(0);
                config.addSource(index, source);
                index++;
                }
            return config;
            }
        return null;
        }

    @Override
    public String asString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(StringConcatenationSource.TYPE_ID))
            {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (ValueSourceConfiguration sub_source : config.getSourceList())
                {
                if (!first)
                    builder.append(" + ");

                List<String> strings = ValueSourceQuickEditSupporters.asStringFromAll(sub_source, project);
                if (strings.size() == 0)
                    return null;

                builder.append(strings.get(0));
                first = false;
                }
            return builder.toString();
            }
        return null;
        }

    @Override
    public int getPriority()
        {
        return 1;
        }
    }


