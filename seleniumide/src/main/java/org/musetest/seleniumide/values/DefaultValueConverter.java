package org.musetest.seleniumide.values;

import org.musetest.core.values.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class DefaultValueConverter implements ValueConverter
    {
    @Override
    public ValueSourceConfiguration convertValue(String parameter)
        {
        return ValueSourceConfiguration.forValue(parameter);
        }
    }


