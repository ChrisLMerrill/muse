package org.musetest.selenium.pages;

import org.musetest.selenium.locators.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class PagesElementValueSourceStringExpressionSupport extends ElementByLocatorValueSourceStringExpressionSupport
    {
    public PagesElementValueSourceStringExpressionSupport()
        {
        super(PagesElementValueSource.TYPE_ID, "page");
        }
    }


