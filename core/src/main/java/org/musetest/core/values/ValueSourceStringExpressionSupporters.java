package org.musetest.core.values;

import org.musetest.core.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceStringExpressionSupporters
    {
    public ValueSourceStringExpressionSupporters(MuseProject project)
        {
        _supporters = project.getClassLocator().getInstances(ValueSourceStringExpressionSupport.class);
        }

    public String toString(ValueSourceConfiguration source, StringExpressionContext context)
	    {
	    return toString(source, context, 0);
	    }

    public String toString(ValueSourceConfiguration source, StringExpressionContext context, int depth)
        {
        for (ValueSourceStringExpressionSupport support : _supporters)
            {
            try
                {
                String stringified = support.toString(source, context, depth);
                if (stringified != null)
                    return stringified;
                }
            catch (Exception e)
                {
                // that's ok, just keep continue looking
                }
            }
        return null;
        }

    private List<ValueSourceStringExpressionSupport> _supporters;
    }


