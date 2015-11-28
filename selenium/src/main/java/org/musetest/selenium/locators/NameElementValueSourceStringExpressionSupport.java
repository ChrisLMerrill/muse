package org.musetest.selenium.locators;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class NameElementValueSourceStringExpressionSupport extends ElementByLocatorValueSourceStringExpressionSupport
    {
    public NameElementValueSourceStringExpressionSupport()
        {
        super(NameElementValueSource.TYPE_ID, "name");
        }
    }


