package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.*;
import org.openqa.selenium.remote.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("phantomjs-provider")
@SuppressWarnings("unused,WeakerAccess")  // used via reflection
public class PhantomjsProvider extends BaseLocalDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        if (getOs() != null && !(OperatingSystem.get().equals(getOs())))
            return null;   // this provider is not for the current OS

        if (!capabilities.getName().equals(BrowserType.PHANTOMJS))
            return null;

        File path = getDriverLocation(context);
        if (path == null)
            {
            context.raiseEvent(MessageEventType.create("PhantomjsProvider would try to satisfy request for PhantomJS browser, but it was not configured with a path to the driver"));
            return null;
            }

        if (!(path.exists()))
            {
            context.raiseEvent(MessageEventType.create("PhantomjsProvider would try to satisfy request for PhantomJS browser, but the configured path does not exist: " + path.getAbsolutePath()));
            return null;
            }

        synchronized (PhantomjsProvider.class)
            {
            DesiredCapabilities desired_caps = capabilities.toDesiredCapabilities();
            desired_caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, path.getAbsolutePath());
            return new PhantomJSDriver(desired_caps);
            }
        }

    @Override
    public String getName()
        {
        return "PhantomJS (local)";
        }

    @Override
    public String toString()
        {
        return "PhantomJS";
        }
    }