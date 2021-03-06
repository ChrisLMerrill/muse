package org.museautomation.selenium.mocks;

import org.museautomation.core.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("muse-mock-provider")
public class MuseMockProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        if (capabilities.getName().equals(MUSE_BROWSER))
            return new MuseMockDriver();
        return null;
        }

    @Override
    public String getName()
        {
        return toString();
        }

    @Override
    public String toString()
        {
        return "MuseMockDriver[local]";
        }

    public final static String MUSE_BROWSER = "musemock";
    }


