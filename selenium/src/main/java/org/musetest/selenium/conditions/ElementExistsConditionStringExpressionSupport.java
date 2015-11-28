package org.musetest.selenium.conditions;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ElementExistsConditionStringExpressionSupport extends SimplePrefixSuffixStringExpressionSupport
    {
    public ElementExistsConditionStringExpressionSupport()
        {
        super(ElementExistsCondition.TYPE_ID, "exists(", ")");
        }

    @Override
    public int getPriority()
        {
        return 2;
        }
    }


