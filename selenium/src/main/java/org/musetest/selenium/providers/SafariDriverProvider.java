package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.safari.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("safaridriver-provider")
public class SafariDriverProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        synchronized (SafariDriverProvider.class)
            {
            if (capabilities.getCapabilities().get(SeleniumBrowserCapabilities.BROWSER_NAME).equals(BrowserType.SAFARI))
                return new SafariDriver(capabilities.toDesiredCapabilities());
            return null;
            }
        }

    @Override
    public String toString()
        {
        return "SafariDriver";
        }
    }


