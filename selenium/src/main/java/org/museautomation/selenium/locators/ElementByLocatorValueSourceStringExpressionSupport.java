package org.museautomation.selenium.locators;

import org.museautomation.core.*;
import org.museautomation.core.values.*;

import java.util.*;

import static org.museautomation.selenium.locators.ElementByLocatorValueSource.MULTIPLE_PARAM;
import static org.museautomation.selenium.locators.ElementByLocatorValueSource.MULTIPLE_SYNTAX;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ElementByLocatorValueSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    protected ElementByLocatorValueSourceStringExpressionSupport(String muse_type_id, String string_expression_type_id)
        {
        _muse_type_id = muse_type_id;
        _string_expression_type_id = string_expression_type_id;
        }

    @Override
    public ValueSourceConfiguration fromElementExpression(String type, List<ValueSourceConfiguration> arguments, MuseProject project)
        {
        if (type.equals(_string_expression_type_id) && arguments.size() > 0)
            {
            ValueSourceConfiguration source = ValueSourceConfiguration.forSource(_muse_type_id, arguments.get(0));
            if (arguments.size() == 2)
                {
                if (MULTIPLE_SYNTAX.equals(arguments.get(1).getValue()))
                    source.addSource(MULTIPLE_PARAM, ValueSourceConfiguration.forValue(true));
                else
                    return null;
                }
            return source;
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
        {
        if (config.getType().equals(_muse_type_id))
            {
            if (config.getSource(MULTIPLE_PARAM) != null && Boolean.TRUE.equals(config.getSource(MULTIPLE_PARAM).getValue()))
                return String.format("<%s:%s,\"%s\">", _string_expression_type_id, context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(), context,depth + 1), MULTIPLE_SYNTAX);
            else
                return String.format("<%s:%s>", _string_expression_type_id, context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(), context,depth + 1));
            }
        return null;
        }

    private final String _string_expression_type_id;
    private final String _muse_type_id;
    }


