package org.musetest.selenium.values;

import org.musetest.builtins.value.*;
import org.musetest.selenium.conditions.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ElementTextStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
    {
    @Override
    public String getName()
        {
        return "elementText";
        }

    @Override
    protected int getNumberArguments()
        {
        return 1;
        }

    @Override
    protected String getTypeId()
        {
        return ElementText.TYPE_ID;
        }

    @Override
    protected boolean storeSingleArgumentAsSingleSubsource()
        {
        return true;
        }
    }


