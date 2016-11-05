package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("chromedriver-provider")
public class ChromeDriverProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        synchronized (ChromeDriverProvider.class)
            {
            if (capabilities.getCapabilities().get(SeleniumBrowserCapabilities.BROWSER_NAME).equals(BrowserType.CHROME))
                {
                if (_path_to_exe == null)
                    {
                    context.raiseEvent(new MessageEvent("ChromeDriverProvider would try to satisfy request for Chrome browser, but it was not provided with a path-to-exe"));
                    return null;
                    }

                if (!(new File(_path_to_exe).exists()))
                    {
                    context.raiseEvent(new MessageEvent("ChromeDriverProvider would try to satisfy request for Chrome browser, but the provided path-to-exe does not exist"));
                    return null;
                    }

                System.setProperty("webdriver.chrome.driver", _path_to_exe);
                return new ChromeDriver(capabilities.toDesiredCapabilities());
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
        return "ChromeDriver";
        }

    private String _path_to_exe;
    }


