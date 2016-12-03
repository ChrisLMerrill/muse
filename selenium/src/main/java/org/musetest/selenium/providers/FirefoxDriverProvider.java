package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("firefoxdriver-provider")
public class FirefoxDriverProvider extends BaseLocalDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        if (getOs() != null && !(OperatingSystem.get().equals(getOs())))
            return null;   // this provider is not for the current OS

        if (!capabilities.getCapabilities().get(SeleniumBrowserCapabilities.BROWSER_NAME).equals(BrowserType.FIREFOX))
            return null;

        synchronized (FirefoxDriverProvider.class)
            {
            return new FirefoxDriver(capabilities.toDesiredCapabilities());
            }
        }

    @Override
    public String toString()
        {
        return "FirefoxDriver";
        }
    }


