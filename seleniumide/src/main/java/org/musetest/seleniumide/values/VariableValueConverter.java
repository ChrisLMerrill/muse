package org.musetest.seleniumide.values;

import org.musetest.builtins.value.*;
import org.musetest.core.values.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // invoked via reflection
public class VariableValueConverter extends PrefixedBracketedValueConverter
    {
    @Override
    protected String getValueSourceTypeId()
        {
        return VariableValueSource.TYPE_ID;
        }

    @Override
    protected String getPrefix()
        {
        return "$";
        }
    }


