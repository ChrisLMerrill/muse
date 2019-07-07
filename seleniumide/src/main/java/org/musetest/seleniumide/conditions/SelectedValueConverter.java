package org.musetest.seleniumide.conditions;

import org.musetest.core.values.*;
import org.musetest.selenium.conditions.*;
import org.musetest.selenium.values.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.locators.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // instantiated via reflection
public class SelectedValueConverter extends ConditionConverter
    {
    public SelectedValueConverter()
        {
        super("SelectedValue");
        }

    @Override
    public ValueSourceConfiguration createConditionSource(String param1, String param2) throws UnsupportedError
        {
        ValueSourceConfiguration text_source = ValueSourceConfiguration.forTypeWithSource(ElementValue.TYPE_ID, LocatorConverters.get().convert(param1));
        return createTextMatchCondition(text_source, param2);
        }
    }


