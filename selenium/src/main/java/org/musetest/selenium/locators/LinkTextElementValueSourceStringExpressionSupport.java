package org.musetest.selenium.locators;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class LinkTextElementValueSourceStringExpressionSupport extends ElementByLocatorValueSourceStringExpressionSupport
    {
    public LinkTextElementValueSourceStringExpressionSupport()
        {
        super(LinkTextElementValueSource.TYPE_ID, STRING_EXPRESSION_ID);
        }

    public static final String STRING_EXPRESSION_ID = "linktext";
    }


