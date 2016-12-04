package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.safari.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("safaridriver-provider")
public class SafariDriverProvider extends BaseLocalDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        if (getOs() != null && !(OperatingSystem.get().equals(getOs())))
            return null;   // this provider is not for the current OS

        if (!capabilities.getName().equals(BrowserType.SAFARI))
            return null;

        synchronized (SafariDriverProvider.class)
            {
            return new SafariDriver(capabilities.toDesiredCapabilities());
            }
        }

    @Override
    public String toString()
        {
        return "SafariDriver";
        }
    }


