package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("firefox-marionette-provider")
public class FirefoxMarionetteDriverProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities)
        {
        synchronized (FirefoxMarionetteDriverProvider.class)
            {
            if (capabilities.getCapabilities().get(SeleniumBrowserCapabilities.BROWSER_NAME).equals(BrowserType.FIREFOX))
                {
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

    private String _path_to_exe;
    }


