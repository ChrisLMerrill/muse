package org.musetest.selenium.conditions;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ElementEnabledConditionStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
    {
    @Override
    public String getName()
        {
        return "elementEnabled";
        }

    @Override
    protected int getNumberArguments()
        {
        return 1;
        }

    @Override
    protected String getTypeId()
        {
        return ElementEnabledCondition.TYPE_ID;
        }

    @Override
    protected boolean storeSingleArgumentAsSingleSubsource()
        {
        return true;
        }
    }


