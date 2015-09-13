package org.musetest.core.values;

import org.musetest.core.*;
import org.musetest.core.util.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceQuickEditSupporters
    {
    /**
     * Utility method for parsing a string with all available implementations.
     */
    public static List<ValueSourceConfiguration> parseWithAll(String string, MuseProject project)
        {
        List<ValueSourceConfiguration> configurations = new ArrayList<>();
        int priority = 1;
        while (priority <= 2)
            {
            for (ValueSourceQuickEditSupport parser : getParsers(project))
                {
                if (parser.getPriority() == priority)
                    {
                    ValueSourceConfiguration config = parser.parse(string, project);
                    if (config != null)
                        configurations.add(config);
                    }
                }
            priority++;
            }
        return configurations;
        }

    /**
     * Utility method for creating a string with all available implementations.
     */
    public static List<String> asStringFromAll(ValueSourceConfiguration source, MuseProject project)
        {
        List<String> strings = new ArrayList<>();
        for (ValueSourceQuickEditSupport parser : getParsers(project))
            {
            try
                {
                String stringified = parser.asString(source, project);
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

    private static List<ValueSourceQuickEditSupport> getParsers(MuseProject project)
        {
        if (PARSERS == null)
            PARSERS = new FactoryLocator(project.getClassLocator()).findFactories(ValueSourceQuickEditSupport.class);
        return PARSERS;
        }

    private static List<ValueSourceQuickEditSupport> PARSERS;
    }


