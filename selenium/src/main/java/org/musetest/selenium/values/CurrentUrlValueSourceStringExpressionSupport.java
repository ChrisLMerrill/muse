package org.musetest.selenium.values;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class CurrentUrlValueSourceStringExpressionSupport extends SimpleWebdriverValueSourceStringExpressionSupport
    {
    public CurrentUrlValueSourceStringExpressionSupport()
        {
        super(NAME, CurrentUrlValueSource.TYPE_ID);
        }

    public final static String NAME = "url";
    }


