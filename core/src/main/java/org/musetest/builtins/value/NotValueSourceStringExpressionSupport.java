package org.musetest.builtins.value;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NotValueSourceStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
    {
    @Override
    public String getName()
        {
        return "not";
        }

    @Override
    protected int getNumberArguments()
        {
        return 1;
        }

    @Override
    protected String getTypeId()
        {
        return NotValueSource.TYPE_ID;
        }
    }


