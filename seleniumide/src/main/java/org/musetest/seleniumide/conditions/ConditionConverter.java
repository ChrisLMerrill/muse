package org.musetest.seleniumide.conditions;

import org.musetest.builtins.condition.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ConditionConverter
    {
    ConditionConverter(String id)
        {
        _id = id;
        }

    String getId()
        {
        return _id;
        }

    public abstract ValueSourceConfiguration createConditionSource(String param1, String param2) throws UnsupportedError;

    private String _id;

    ValueSourceConfiguration createTextMatchCondition(ValueSourceConfiguration left_operand, String param)
        {
        if (param.startsWith(REGEXP_PREFIX) || param.startsWith(REGEXPI_PREFIX))
            {
            boolean case_insensitive;
            if (param.startsWith(REGEXP_PREFIX))
                {
                param = param.substring(REGEXP_PREFIX.length());
                case_insensitive = false;
                }
            else
                {
                param = param.substring(REGEXPI_PREFIX.length());
                case_insensitive = true;
                }

            ValueSourceConfiguration source = ValueSourceConfiguration.forType(RegexMatchCondition.TYPE_ID);
            source.addSource(RegexMatchCondition.TARGET_PARAM, left_operand);
            source.addSource(RegexMatchCondition.PATTERN_PARAM, ValueSourceConfiguration.forValue(param));
            if (case_insensitive)
                source.addSource(RegexMatchCondition.CASE_PARAM, ValueSourceConfiguration.forValue(true));
            return source;
            }

        // is it a glob match or exact?
        Boolean is_glob_pattern = null;
        String match_pattern = null;
        if (param.startsWith(GLOB_PREFIX))
            {
            is_glob_pattern = true;
            match_pattern = param.substring(GLOB_PREFIX.length());
            }
        else if (param.startsWith(EXACT_PREFIX))
            {
            is_glob_pattern = false;
            match_pattern = param.substring(EXACT_PREFIX.length());
            }

        if (is_glob_pattern == null)
            {
            // still not sure, yet
            if (new GlobPattern(param).hasWildcard())
                is_glob_pattern = true;
            else
                is_glob_pattern = false;
            match_pattern = param;
            }

        if (is_glob_pattern)
            {
            ValueSourceConfiguration source = ValueSourceConfiguration.forType(GlobMatchCondition.TYPE_ID);
            source.addSource(GlobMatchCondition.TARGET_PARAM, left_operand);
            source.addSource(GlobMatchCondition.PATTERN_PARAM, ValueSourceConfiguration.forValue(match_pattern));
            return source;
            }
        else
            {
            ValueSourceConfiguration source = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
            source.addSource(EqualityCondition.LEFT_PARAM, left_operand);
            source.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forValue(match_pattern));
            return source;
            }
        }

    private final static String GLOB_PREFIX = "glob:";
    private final static String EXACT_PREFIX = "exact:";
    private final static String REGEXP_PREFIX = "regexp:";
    private final static String REGEXPI_PREFIX = "regexpi:";
    }


