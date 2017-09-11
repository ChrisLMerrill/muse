package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.remote.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("edgedriver-provider")
@SuppressWarnings("unused")  // used via reflection
public class EdgeDriverProvider extends BaseLocalDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        if (getOs() != null && !(OperatingSystem.get().equals(getOs())))
            return null;   // this provider is not for the current OS

        if (!capabilities.getName().equals(BrowserType.EDGE))
            return null;

        File path = getDriverLocation(context);
        if (path == null)
            {
            context.raiseEvent(new MessageEvent("EdgeDriverProvider would try to satisfy request for Firefox browser, but it was not configured with a path to the driver"));
            return null;
            }

        if (!(path.exists()))
            {
            context.raiseEvent(new MessageEvent("EdgeDriverProvider would try to satisfy request for Internet Explorer browser, but the configured path does not exist: " + path.getAbsolutePath()));
            return null;
            }

        synchronized (EdgeDriverProvider.class)
            {
            System.setProperty("webdriver.edge.driver", path.getAbsolutePath());
            return new EdgeDriver(capabilities.toDesiredCapabilities());
            }
        }

    @Override
    public String getName()
        {
        return "Microsoft Webdriver for Edge (local)";
        }

    @Override
    public String toString()
        {
        return "EdgeDriver";
        }
    }


