package org.musetest.selenium.locators;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class IdElementValueSourceStringExpressionSupport extends ElementByLocatorValueSourceStringExpressionSupport
    {
    public IdElementValueSourceStringExpressionSupport()
        {
        super(IdElementValueSource.TYPE_ID, STRING_EXPRESSION_ID);
        }

    public static final String STRING_EXPRESSION_ID = "id";
    }


