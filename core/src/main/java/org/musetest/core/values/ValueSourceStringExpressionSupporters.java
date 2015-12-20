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
        for (ValueSourceStringExpressionSupport support : getSupporters(project))
            {
            try
                {
                String stringified = support.toString(source, project);
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


