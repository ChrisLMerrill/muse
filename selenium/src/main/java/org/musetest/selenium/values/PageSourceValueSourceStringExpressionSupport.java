package org.musetest.selenium.values;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class PageSourceValueSourceStringExpressionSupport extends SimpleWebdriverValueSourceStringExpressionSupport
    {
    public PageSourceValueSourceStringExpressionSupport()
        {
        super(NAME, PageSourceValueSource.TYPE_ID);
        }

    public final static String NAME = "source";
    }


