package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class AdditionSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromBinaryExpression(ValueSourceConfiguration left, String operator, ValueSourceConfiguration right, MuseProject project)
        {
        if (!operator.equals("+"))
            return null;

        ValueSourceConfiguration new_config = new ValueSourceConfiguration();
        new_config.setType(AdditionSource.TYPE_ID);

        // add everything on the left
        if (left.getType().equals(AdditionSource.TYPE_ID))
            for (ValueSourceConfiguration left_subsource : left.getSourceList())
                new_config.addSource(left_subsource);
        else
            new_config.addSource(left);

        // add everything on the right
        if (right.getType().equals(AdditionSource.TYPE_ID))
            for (ValueSourceConfiguration right_subsource : right.getSourceList())
                new_config.addSource(right_subsource);
        else
            new_config.addSource(right);

        return new_config;
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


