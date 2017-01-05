package org.musetest.builtins.value;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class RandomNumberValueSourceStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
    {
    @Override
    public String getName()
        {
        return "random";
        }

    @Override
    protected int getNumberArguments()
        {
        return 2;
        }

    @Override
    protected boolean storeArgumentsNamed()
        {
        return true;
        }

    @Override
    protected String[] getArgumentNames()
        {
        return new String[] { RandomNumberValueSource.MIN_PARAM, RandomNumberValueSource.MAX_PARAM };
        }

    @Override
    protected String getTypeId()
        {
        return RandomNumberValueSource.TYPE_ID;
        }

    @Override
    protected boolean storeSingleArgumentAsSingleSubsource()
        {
        return true;
        }
    }


