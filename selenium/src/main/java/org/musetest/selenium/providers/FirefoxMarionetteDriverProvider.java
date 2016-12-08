package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("firefox-marionette-provider")
public class FirefoxMarionetteDriverProvider extends BaseLocalDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        if (getOs() != null && !(OperatingSystem.get().equals(getOs())))
            return null;   // this provider is not for the current OS

        if (!capabilities.getName().equals(BrowserType.FIREFOX))
            return null;

        File path = getDriverLocation(context);
        if (path == null)
            {
            context.raiseEvent(new MessageEvent("FirefoxMarionetteDriverProvider would try to satisfy request for Firefox browser, but it was not configured with a path to the driver"));
            return null;
            }

        if (!(path.exists()))
            {
            context.raiseEvent(new MessageEvent("FirefoxMarionetteDriverProvider would try to satisfy request for Firefox browser, but the configured path does not exist: " + path.getAbsolutePath()));
            return null;
            }

        synchronized (FirefoxMarionetteDriverProvider.class)
            {
            System.setProperty("webdriver.gecko.driver", path.getAbsolutePath());
            DesiredCapabilities selenium_capabilities = capabilities.toDesiredCapabilities();
            selenium_capabilities.setCapability("marionette", true);
            return new MarionetteDriver(selenium_capabilities);
            }
        }

    @Override
    public String getName()
        {
        return "Firefox Marionette (local)";
        }

    @Override
    public String toString()
        {
        return "MarionetteDriver";
        }
    }


