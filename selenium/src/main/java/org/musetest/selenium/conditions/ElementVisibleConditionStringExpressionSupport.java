package org.musetest.selenium.conditions;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ElementVisibleConditionStringExpressionSupport extends SimplePrefixSuffixStringExpressionSupport
    {
    public ElementVisibleConditionStringExpressionSupport()
        {
        super(ElementVisibleCondition.TYPE_ID, "visible(", ")");
        }

    @Override
    public int getPriority()
        {
        return 2;
        }
    }


