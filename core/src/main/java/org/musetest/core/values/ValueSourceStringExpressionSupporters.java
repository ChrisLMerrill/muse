package org.musetest.core.values;

import org.musetest.core.*;
import org.musetest.core.util.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceStringExpressionSupporters
    {
    public static String toString(ValueSourceConfiguration source, MuseProject project)
        {
        return toString(source, project, 0);
        }

    public static String toString(ValueSourceConfiguration source, MuseProject project, int depth)
        {
        for (ValueSourceStringExpressionSupport support : getSupporters(project))
            {
            try
                {
                String stringified = support.toString(source, project, depth);
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

    private static List<ValueSourceStringExpressionSupport> getSupporters(MuseProject project)
        {
        if (SUPPORTERS == null)
            SUPPORTERS = new FactoryLocator(project.getClassLocator()).findFactories(ValueSourceStringExpressionSupport.class);
        return SUPPORTERS;
        }

    private static List<ValueSourceStringExpressionSupport> SUPPORTERS;
    }


