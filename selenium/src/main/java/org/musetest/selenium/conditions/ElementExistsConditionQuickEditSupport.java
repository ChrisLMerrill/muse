package org.musetest.selenium.conditions;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ElementExistsConditionQuickEditSupport extends SimplePrefixSuffixQuickEditSupport
    {
    public ElementExistsConditionQuickEditSupport()
        {
        super(ElementExistsCondition.TYPE_ID, "exists(", ")");
        }

    @Override
    public int getPriority()
        {
        return 2;
        }
    }


