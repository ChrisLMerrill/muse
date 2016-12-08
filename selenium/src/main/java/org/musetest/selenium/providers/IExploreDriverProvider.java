package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.remote.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("iexploredriver-provider")
public class IExploreDriverProvider extends BaseLocalDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        if (getOs() != null && !(OperatingSystem.get().equals(getOs())))
            return null;   // this provider is not for the current OS

        if (!capabilities.getName().equals(BrowserType.IEXPLORE))
            return null;

        File path = getDriverLocation(context);
        if (path == null)
            {
            context.raiseEvent(new MessageEvent("IExploreDriverProvider would try to satisfy request for Firefox browser, but it was not configured with a path to the driver"));
            return null;
            }

        if (!(path.exists()))
            {
            context.raiseEvent(new MessageEvent("IExploreDriverProvider would try to satisfy request for Internet Explorer browser, but the configured path does not exist: " + path.getAbsolutePath()));
            return null;
            }

        synchronized (IExploreDriverProvider.class)
            {
            System.setProperty("webdriver.ie.driver", path.getAbsolutePath());
            return new InternetExplorerDriver(capabilities.toDesiredCapabilities());
            }
        }

    @Override
    public String getName()
        {
        return "IEDriverServer (local)";
        }

    @Override
    public String toString()
        {
        return "InternetExplorerDriver";
        }
    }


