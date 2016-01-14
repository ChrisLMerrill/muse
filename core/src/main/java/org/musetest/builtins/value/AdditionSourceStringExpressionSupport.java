package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class AdditionSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromBinaryExpression(ValueSourceConfiguration left, String operator, ValueSourceConfiguration right, MuseProject project)
        {
        if (!"+".equals(operator))
            return null;

        List<ValueSourceConfiguration> sources = new ArrayList<>();
        addSources(sources, left);
        addSources(sources, right);

        ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
        config.setSourceList(sources);
        return config;
        }

    private void addSources(List<ValueSourceConfiguration> list, ValueSourceConfiguration source)
        {
        if (AdditionSource.TYPE_ID.equals(source.getType()))
            for (ValueSourceConfiguration subsource : source.getSourceList())
                list.add(subsource);
        else
            list.add(source);
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (config.getType().equals(AdditionSource.TYPE_ID))
            {
            StringBuilder builder = new StringBuilder();
            if (depth > 0)
                builder.append("(");
            boolean first = true;
            for (ValueSourceConfiguration sub_source : config.getSourceList())
                {
                if (!first)
                    builder.append(" + ");

                String stringified = project.getValueSourceStringExpressionSupporters().toString(sub_source, depth + 1);
                if (stringified == null)
                    return null;
                builder.append(stringified);
                first = false;
                }
            if (depth > 0)
                builder.append(")");
            return builder.toString();
            }
        return null;
        }
    }


