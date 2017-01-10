package org.musetest.seleniumide.conditions;

import org.musetest.builtins.condition.*;
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

    protected ValueSourceConfiguration createTextMatchCondition(ValueSourceConfiguration left_operand, String param)
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
        source.addSource(EqualityCondition.LEFT_PARAM, left_operand);
        source.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forValue(param));
        return source;
        }
    }


