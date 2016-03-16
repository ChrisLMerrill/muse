package org.musetest.selenium.conditions;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ElementEnabledConditionStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromArgumentedExpression(String name, List<ValueSourceConfiguration> arguments, MuseProject project)
        {
        if (NAME.equals(name) && arguments.size() == 1)
            {
            ValueSourceConfiguration config = ValueSourceConfiguration.forType(ElementEnabledCondition.TYPE_ID);
            config.setSource(arguments.get(0));
            return config;
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (ElementEnabledCondition.TYPE_ID.equals(config.getType()))
            return String.format("%s(%s)", NAME, project.getValueSourceStringExpressionSupporters().toString(config.getSource(), depth + 1));
        else
            return null;
        }

    public final static String NAME = "elementEnabled";
    }


