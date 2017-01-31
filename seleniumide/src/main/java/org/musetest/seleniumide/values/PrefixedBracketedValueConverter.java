package org.musetest.seleniumide.values;

import org.musetest.core.values.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused,WeakerAccess") // invoked via reflection
public abstract class PrefixedBracketedValueConverter implements ValueConverter
    {
    @Override
    public ValueSourceConfiguration convert(String parameter)
        {
        String full_prefix = getPrefix() + getOpenBracketCharacter();
        String suffix = getCloseBracketCharacter();

        if (parameter.startsWith(full_prefix) && parameter.endsWith(suffix))
            {
            String name = parameter.substring(full_prefix.length(), parameter.length() - suffix.length());
            ValueSourceConfiguration name_source = ValueSourceConfiguration.forValue(name);
            return ValueSourceConfiguration.forTypeWithSource(getValueSourceTypeId(), name_source);
            }
        return null;
        }

    protected abstract String getValueSourceTypeId();
    protected abstract String getPrefix();
    protected String getOpenBracketCharacter()
        {
        return "{";
        }
    protected String getCloseBracketCharacter()
        {
        return "}";
        }
    }


