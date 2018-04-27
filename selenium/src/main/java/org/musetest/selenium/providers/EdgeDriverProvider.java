package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

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
            context.raiseEvent(MessageEventType.create("EdgeDriverProvider would try to satisfy request for Firefox browser, but it was not configured with a path to the driver"));
            return null;
            }

        if (!(path.exists()))
            {
            context.raiseEvent(MessageEventType.create("EdgeDriverProvider would try to satisfy request for Internet Explorer browser, but the configured path does not exist: " + path.getAbsolutePath()));
            return null;
            }

        synchronized (EdgeDriverProvider.class)
            {

            DesiredCapabilities desired = DesiredCapabilities.edge();
            if (capabilities.getVersion() != null && capabilities.getVersion().length() > 0)
                desired.setVersion(capabilities.getVersion());
            if (capabilities.getPlatform() != null && capabilities.getPlatform().length() > 0)
                desired.setPlatform(Platform.fromString(capabilities.getPlatform()));

            EdgeOptions options = new EdgeOptions();
            options.merge(desired);
            if (getArguments() != null)
            	LOG.error("Unable to set arguments for EdgeDriver: arguments are not supported by EdgeDriver");

            System.setProperty("webdriver.edge.driver", path.getAbsolutePath());
            return new EdgeDriver(options);
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

    private final static Logger LOG = LoggerFactory.getLogger(EdgeDriverProvider.class);
    }