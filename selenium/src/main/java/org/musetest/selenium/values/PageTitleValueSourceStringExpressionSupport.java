package org.musetest.selenium.values;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class PageTitleValueSourceStringExpressionSupport extends SimpleElementValueSourceStringExpressionSupport
    {
    public PageTitleValueSourceStringExpressionSupport()
        {
        super(NAME, PageTitleValueSource.TYPE_ID);
        }

    public final static String NAME = "title";
    }


