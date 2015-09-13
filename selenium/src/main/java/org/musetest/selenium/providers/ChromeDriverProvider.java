package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("chromedriver-provider")
public class ChromeDriverProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities)
        {
        synchronized (ChromeDriverProvider.class)
            {
            if (capabilities.getCapabilities().get(SeleniumBrowserCapabilities.BROWSER_NAME).equals(BrowserType.CHROME))
                {
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

    private String _path_to_exe;
    }


