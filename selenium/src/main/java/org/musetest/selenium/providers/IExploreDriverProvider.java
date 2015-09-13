package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.remote.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("iexploredriver-provider")
public class IExploreDriverProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities)
        {
        synchronized (IExploreDriverProvider.class)
            {
            if (capabilities.getCapabilities().get(SeleniumBrowserCapabilities.BROWSER_NAME).equals(BrowserType.IEXPLORE))
                {
                System.setProperty("webdriver.ie.driver", _path_to_exe);
                return new InternetExplorerDriver(capabilities.toDesiredCapabilities());
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


