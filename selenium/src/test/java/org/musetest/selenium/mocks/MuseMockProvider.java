package org.musetest.selenium.mocks;

import org.musetest.core.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("muse-mock-provider")
public class MuseMockProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities)
        {
        if (capabilities.getCapabilities().get(SeleniumBrowserCapabilities.BROWSER_NAME).equals(MUSE_BROWSER))
            return new MuseMockDriver();
        return null;
        }

    public final static String MUSE_BROWSER = "musemock";
    }


