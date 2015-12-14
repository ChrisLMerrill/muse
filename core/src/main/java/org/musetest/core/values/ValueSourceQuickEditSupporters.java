package org.musetest.core.values;

import org.musetest.core.*;
import org.musetest.core.util.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceQuickEditSupporters
    {
    @Deprecated
    public static List<ValueSourceConfiguration> parseWithAll(String string, MuseProject project)
        {
        List<ValueSourceConfiguration> configurations = new ArrayList<>();
        int priority = 1;
        while (priority <= 2)
            {
            for (ValueSourceStringExpressionSupport parser : getParsers(project))
                {
                if (parser.getPriority() == priority)
                    {
                    ValueSourceConfiguration config = parser.fromLiteral(string, project);
                    if (config != null)
                        configurations.add(config);
                    }
                }
            priority++;
            }
        return configurations;
        }

    @Deprecated
    public static List<String> asStringFromAll(ValueSourceConfiguration source, MuseProject project)
        {
        List<String> strings = new ArrayList<>();
        for (ValueSourceStringExpressionSupport parser : getParsers(project))
            {
            try
                {
                String stringified = parser.toString(source, project);
                if (stringified != null)
                    strings.add(stringified);
                }
            catch (Exception e)
                {
                // that's ok, just keep continue looking
                }
            }
        return strings;
        }

    private static List<ValueSourceStringExpressionSupport> getParsers(MuseProject project)
        {
        if (PARSERS == null)
            PARSERS = new FactoryLocator(project.getClassLocator()).findFactories(ValueSourceStringExpressionSupport.class);
        return PARSERS;
        }

    private static List<ValueSourceStringExpressionSupport> PARSERS;
    }


