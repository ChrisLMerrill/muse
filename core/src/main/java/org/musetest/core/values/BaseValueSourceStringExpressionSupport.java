package org.musetest.core.values;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseValueSourceStringExpressionSupport implements ValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
        {
        return null;
        }

    @Override
    public ValueSourceConfiguration fromPrefixedExpression(String prefix, ValueSourceConfiguration expression, MuseProject project)
        {
        return null;
        }

    @Override
    public ValueSourceConfiguration fromElementExpression(String type, ValueSourceConfiguration qualifier, MuseProject project)
        {
        return null;
        }

    @Override
    public int getPriority()
        {
        return 0;
        }
    }


