package org.musetest.selenium.locators;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

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
        if (type.equals(_string_expression_type_id) && arguments.size() == 1)
            return ValueSourceConfiguration.forSource(_muse_type_id, arguments.get(0));
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(_muse_type_id))
            return String.format("<%s:%s>", _string_expression_type_id, ValueSourceStringExpressionSupporters.toString(config.getSource(), project));
        return null;
        }

    @Override
    public int getPriority()
        {
        return 2;
        }

    private String _string_expression_type_id;
    private String _muse_type_id;
    }


