package org.musetest.builtins.condition;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class GreaterThanConditionStringExpressionSupport extends BinaryConditionStringExpressionSupport
    {
    @Override
    public String getOperator()
        {
        return ">";
        }

    @Override
    public String getSourceType()
        {
        return GreaterThanCondition.TYPE_ID;
        }
    }


