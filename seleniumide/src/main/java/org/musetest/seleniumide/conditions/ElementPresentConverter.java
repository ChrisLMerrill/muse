package org.musetest.seleniumide.conditions;

import org.musetest.core.values.*;
import org.musetest.selenium.conditions.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.locators.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // instantiated via reflection
public class ElementPresentConverter extends ConditionConverter
    {
    public ElementPresentConverter()
        {
        super("ElementPresent");
        }

    @Override
    public ValueSourceConfiguration createConditionSource(String param1, String param2) throws UnsupportedError
        {
        return ValueSourceConfiguration.forTypeWithSource(ElementExistsCondition.TYPE_ID, LocatorConverters.get().convert(param1));
        }
    }


