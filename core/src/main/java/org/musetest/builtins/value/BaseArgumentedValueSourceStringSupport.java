package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseArgumentedValueSourceStringSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromArgumentedExpression(String name, List<ValueSourceConfiguration> arguments, MuseProject project)
        {
        if (getName().equals(name) && arguments.size() == getNumberArguments())
            {
            ValueSourceConfiguration config = ValueSourceConfiguration.forType(getTypeId());

            if (arguments.size() == 1 && storeSingleArgumentAsSingleSubsource())
                config.setSource(arguments.get(0));
            else
                {
                for (int i = 0; i < arguments.size(); i++)
                    config.addSource(i, arguments.get(i));
                }

            return config;
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (getTypeId().equals(config.getType()))
            {
            StringBuilder builder = new StringBuilder();
            builder.append(getName());
            builder.append('(');

            if (getNumberArguments() == 1 && storeSingleArgumentAsSingleSubsource())
                builder.append(project.getValueSourceStringExpressionSupporters().toString(config.getSource()));
            else
                {
                int arguments = 0;
                for (ValueSourceConfiguration argument : config.getSourceList())
                    {
                    if (arguments > 0)
                        builder.append(',');
                    builder.append(project.getValueSourceStringExpressionSupporters().toString(argument));
                    arguments++;
                    }
                }

            builder.append(')');
            return builder.toString();
            }
        else
            return null;
        }

    public abstract String getName();
    protected abstract int getNumberArguments();
    protected abstract String getTypeId();

    /**
     * Override and return true to store a single argument as the singular subsource (accessed via getSource()) instead of
     * a list of one (accessed via getSourceList()).
     */
    protected boolean storeSingleArgumentAsSingleSubsource()
        {
        return false;
        }
    }


