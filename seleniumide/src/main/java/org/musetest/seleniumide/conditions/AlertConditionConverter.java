package org.musetest.seleniumide.conditions;

import org.musetest.core.values.*;
import org.musetest.selenium.values.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.locators.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // instantiated via reflection
public class AlertConditionConverter extends ConditionConverter
    {
    public AlertConditionConverter()
        {
        super("Alert");
        }

    AlertConditionConverter(String type)
        {
        super(type);
        }

    @Override
    public ValueSourceConfiguration createConditionSource(String param1, String param2)
        {
        ValueSourceConfiguration text_source = ValueSourceConfiguration.forType(DialogTextSource.TYPE_ID);
        return createTextMatchCondition(text_source, param1);
        }
    }


