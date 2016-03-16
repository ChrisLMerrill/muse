package org.musetest.selenium.conditions;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ElementVisibleConditionStringExpressionSupport extends BaseElementConditionStringExpressionSupport
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
    }


