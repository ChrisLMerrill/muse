package org.musetest.seleniumide.locators;

import org.musetest.core.values.*;
import org.musetest.selenium.locators.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection by LocatorConverters
public class CssSelectorConverter implements LocatorConverter
    {
    @Override
    public ValueSourceConfiguration createLocator(String parameter)
        {
        if (parameter.startsWith(PREFIX))
            return ValueSourceConfiguration.forTypeWithSource(CssElementValueSource.TYPE_ID, parameter.substring(PREFIX.length()));
        return null;
        }

    private final static String PREFIX = "css=";
    }


