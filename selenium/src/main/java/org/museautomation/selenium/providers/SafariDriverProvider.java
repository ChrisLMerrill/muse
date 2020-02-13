package org.museautomation.selenium.providers;

import org.museautomation.core.*;
import org.museautomation.selenium.*;
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
        if (!capabilities.getName().equals(BrowserType.SAFARI))
            return null;

        synchronized (SafariDriverProvider.class)
            {
            return new SafariDriver(capabilities.toDesiredCapabilities());
            }
        }

    @Override
    public String getName()
        {
        return "Safari (local)";
        }

    @Override
    public String toString()
        {
        return "SafariDriver";
        }
    }


