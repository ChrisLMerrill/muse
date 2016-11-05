package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("firefox-marionette-provider")
public class FirefoxMarionetteDriverProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        synchronized (FirefoxMarionetteDriverProvider.class)
            {
            if (capabilities.getCapabilities().get(SeleniumBrowserCapabilities.BROWSER_NAME).equals(BrowserType.FIREFOX))
                {
                if (_path_to_exe == null)
                    {
                    context.raiseEvent(new MessageEvent("FirefoxMarionetteDriverProvider would try to satisfy request for Firefox browser, but it was not provided with a path-to-exe"));
                    return null;
                    }

                if (!(new File(_path_to_exe).exists()))
                    {
                    context.raiseEvent(new MessageEvent("FirefoxMarionetteDriverProvider would try to satisfy request for Firefox browser, but the provided path-to-exe does not exist"));
                    return null;
                    }

                System.setProperty("webdriver.gecko.driver", _path_to_exe);
                DesiredCapabilities selenium_capabilities = capabilities.toDesiredCapabilities();
                selenium_capabilities.setCapability("marionette", true);
                return new MarionetteDriver(selenium_capabilities);
                }
            return null;
            }
        }

    public String getPathToExe()
        {
        return _path_to_exe;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setPathToExe(String path_to_exe)
        {
        _path_to_exe = path_to_exe;
        }

    @Override
    public String toString()
        {
        return "MarionetteDriver";
        }

    private String _path_to_exe;
    }


