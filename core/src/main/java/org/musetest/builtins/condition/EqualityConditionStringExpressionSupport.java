package org.musetest.builtins.condition;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class EqualityConditionStringExpressionSupport extends BinaryConditionStringExpressionSupport
    {
    @Override
    protected String getSeparator()
        {
        return "=";
        }

    @Override
    protected String getSourceType()
        {
        return "equals";
        }
    }


