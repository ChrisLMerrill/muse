package org.musetest.selenium.locators;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class XPathElementValueSourceStringExpressionSupport extends ElementByLocatorValueSourceStringExpressionSupport
    {
    public XPathElementValueSourceStringExpressionSupport()
        {
        super(XPathElementValueSource.TYPE_ID, "xpath");
        }
    }


