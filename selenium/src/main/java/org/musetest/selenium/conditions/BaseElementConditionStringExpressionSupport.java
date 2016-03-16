package org.musetest.selenium.conditions;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseElementConditionStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromArgumentedExpression(String name, List<ValueSourceConfiguration> arguments, MuseProject project)
        {
        if (getName().equals(name) && arguments.size() == getNumberArguments())
            {
            ValueSourceConfiguration config = ValueSourceConfiguration.forType(getTypeId());
            config.setSource(arguments.get(0));
            return config;
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (getTypeId().equals(config.getType()))
            return String.format("%s(%s)", getName(), project.getValueSourceStringExpressionSupporters().toString(config.getSource(), depth + 1));
        else
            return null;
        }

    public abstract String getName();
    protected abstract int getNumberArguments();
    protected abstract String getTypeId();
    }


