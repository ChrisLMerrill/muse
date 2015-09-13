package org.musetest.builtins.condition;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class GreaterThanConditionQuickEditSupport extends BinaryConditionQuickEditSupport
    {
    @Override
    protected String getSeparator()
        {
        return ">";
        }

    @Override
    protected String getSourceType()
        {
        return "greaterthan";
        }
    }


