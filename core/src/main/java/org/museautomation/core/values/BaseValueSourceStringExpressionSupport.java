package org.museautomation.core.values;

import org.museautomation.core.*;

import java.util.*;

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
    public ValueSourceConfiguration fromElementExpression(String type, List<ValueSourceConfiguration> arguments, MuseProject project)
        {
        return null;
        }

    @Override
    public ValueSourceConfiguration fromElementLookupExpression(List<ValueSourceConfiguration> arguments, MuseProject project)
        {
        return null;
        }

    @Override
    public ValueSourceConfiguration fromArgumentedExpression(String name, List<ValueSourceConfiguration> arguments, MuseProject project)
        {
        return null;
        }

    @Override
    public ValueSourceConfiguration fromBinaryExpression(ValueSourceConfiguration left, String operator, ValueSourceConfiguration right, MuseProject project)
        {
        return null;
        }

    @Override
    public ValueSourceConfiguration fromBooleanExpression(ValueSourceConfiguration left, String operator, ValueSourceConfiguration right, MuseProject project)
        {
        return null;
        }

    @Override
    public ValueSourceConfiguration fromDotExpression(ValueSourceConfiguration left, ValueSourceConfiguration right, MuseProject project)
        {
        return null;
        }

    @Override
    public ValueSourceConfiguration fromArrayExpression(List<ValueSourceConfiguration> elements, MuseProject project)
	    {
	    return null;
	    }

    @Override
    public ValueSourceConfiguration fromArrayItemExpression(ValueSourceConfiguration collection, ValueSourceConfiguration selector, MuseProject project)
        {
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, StringExpressionContext context)
        {
        return toString(config, context, 0);
        }
    }
