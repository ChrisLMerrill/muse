package org.musetest.selenium.values;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // used via reflection
public abstract class SimpleWebdriverValueSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    public SimpleWebdriverValueSourceStringExpressionSupport(String expression_name, String muse_type)
        {
        _name = expression_name;
        _muse_type = muse_type;
        }

    @Override
    public ValueSourceConfiguration fromElementExpression(String type, List<ValueSourceConfiguration> arguments, MuseProject project)
        {
        if (_name.equals(type) && arguments.size() == 0)
            return ValueSourceConfiguration.forType(_muse_type);
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
        {
        if (_muse_type.equals(config.getType()))
            return "<" + _name + ">";
        return null;
        }

    private final String _name;
    private final String _muse_type;
    }


