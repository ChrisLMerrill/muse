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
        _project = project;
        _supporters = project.getClassLocator().getInstances(ValueSourceStringExpressionSupport.class);
        }

    public String toString(ValueSourceConfiguration source)
        {
        return toString(source, 0);
        }

    public String toString(ValueSourceConfiguration source, int depth)
        {
        for (ValueSourceStringExpressionSupport support : _supporters)
            {
            try
                {
                String stringified = support.toString(source, _project, depth);
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
    private MuseProject _project;
    }


