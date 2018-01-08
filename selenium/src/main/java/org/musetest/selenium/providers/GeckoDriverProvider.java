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
public class GeckoDriverProvider extends BaseLocalDriverProvider
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
            context.raiseEvent(MessageEventType.create("GeckoDriverProvider would try to satisfy request for Firefox browser, but it was not configured with a path to the driver"));
            return null;
            }

        if (!(path.exists()))
            {
            context.raiseEvent(MessageEventType.create("GeckoDriverProvider would try to satisfy request for Firefox browser, but the configured path does not exist: " + path.getAbsolutePath()));
            return null;
            }

        synchronized (GeckoDriverProvider.class)
            {
            System.setProperty("webdriver.gecko.driver", path.getAbsolutePath());
            DesiredCapabilities selenium_capabilities = DesiredCapabilities.firefox();
            if (capabilities.getVersion() != null && capabilities.getVersion().length() > 0)
                selenium_capabilities.setVersion(capabilities.getVersion());
            if (capabilities.getPlatform() != null && capabilities.getPlatform().length() > 0)
                selenium_capabilities.setPlatform(Platform.fromString(capabilities.getPlatform()));
            selenium_capabilities.setCapability("marionette", true);
            return new FirefoxDriver(selenium_capabilities);
            }
        }

    @Override
    public String getName()
        {
        return "GeckoDriver for Firefox (local)";
        }

    @Override
    public String toString()
        {
        return "GeckoDriver";
        }
    }