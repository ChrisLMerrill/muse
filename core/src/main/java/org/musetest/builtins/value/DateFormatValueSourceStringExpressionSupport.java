package org.musetest.builtins.value;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class DateFormatValueSourceStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
    {
    @Override
    public String getName()
        {
        return "formatDate";
        }

    @Override
    protected int getNumberArguments()
        {
        return 2;
        }

    @Override
    protected String getTypeId()
        {
        return DateFormatValueSource.TYPE_ID;
        }

    @Override
    protected boolean storeArgumentsNamed()
        {
        return true;
        }

    @Override
    protected String[] getArgumentNames()
        {
        return new String[] {DateFormatValueSource.DATE_PARAM, DateFormatValueSource.FORMAT_PARAM };
        }
    }


