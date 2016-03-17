package org.musetest.selenium.conditions;

import org.musetest.builtins.value.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ElementVisibleConditionStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
    {
    @Override
    public String getName()
        {
        return "elementVisible";
        }

    @Override
    protected int getNumberArguments()
        {
        return 1;
        }

    @Override
    protected String getTypeId()
        {
        return ElementVisibleCondition.TYPE_ID;
        }

    @Override
    protected boolean storeSingleArgumentAsSingleSubsource()
        {
        return true;
        }
    }


