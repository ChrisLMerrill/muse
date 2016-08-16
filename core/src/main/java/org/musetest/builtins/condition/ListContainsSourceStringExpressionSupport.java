package org.musetest.builtins.condition;

import org.musetest.builtins.value.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class ListContainsSourceStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
    {
    @Override
    public String getName()
        {
        return "listContains";
        }

    @Override
    protected int getNumberArguments()
        {
        return 2;
        }

    @Override
    protected String[] getArgumentNames()
        {
        return new String[] {"list", "target"};
        }

    @Override
    protected String getTypeId()
        {
        return ListContainsSource.TYPE_ID;
        }
    }


