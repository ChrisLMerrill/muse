package org.musetest.seleniumide.values;

import org.musetest.builtins.value.*;
import org.musetest.core.values.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // invoked via reflection
public class VariableValueConverter implements ValueConverter
    {
    @Override
    public ValueSourceConfiguration convert(String parameter)
        {
        if (parameter.startsWith("${") && parameter.endsWith("}"))
            {
            String name = parameter.substring(2, parameter.length() - 1);
            ValueSourceConfiguration name_source = ValueSourceConfiguration.forValue(name);
            return ValueSourceConfiguration.forTypeWithSource(VariableValueSource.TYPE_ID, name_source);
            }
        return null;
        }
    }


