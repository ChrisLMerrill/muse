package org.musetest.seleniumide.conditions;

import org.musetest.builtins.value.logic.*;
import org.musetest.core.values.*;
import org.musetest.selenium.conditions.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.locators.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // instantiated via reflection
public class ElementNotPresentConverter extends ConditionConverter
    {
    public ElementNotPresentConverter()
        {
        super("ElementNotPresent");
        }

    @Override
    public ValueSourceConfiguration createConditionSource(String param1, String param2) throws UnsupportedError
        {
        return ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, new ElementPresentConverter().createConditionSource(param1, param2));
        }
    }


