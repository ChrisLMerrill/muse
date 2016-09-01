package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("firefoxdriver-provider")
public class FirefoxDriverProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities)
        {
        synchronized (FirefoxDriverProvider.class)
            {
            if (capabilities.getCapabilities().get(SeleniumBrowserCapabilities.BROWSER_NAME).equals(BrowserType.FIREFOX))
                return new FirefoxDriver(capabilities.toDesiredCapabilities());
            return null;
            }
        }

    @Override
    public String toString()
        {
        return "FirefoxDriver";
        }
    }


