package org.musetest.selenium.conditions;

import org.musetest.builtins.value.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ElementSelectedConditionStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
    {
    @Override
    public String getName()
        {
        return "elementSelected";
        }

    @Override
    protected int getNumberArguments()
        {
        return 1;
        }

    @Override
    protected String getTypeId()
        {
        return ElementSelectedCondition.TYPE_ID;
        }

    @Override
    protected boolean storeSingleArgumentAsSingleSubsource()
        {
        return true;
        }
    }


