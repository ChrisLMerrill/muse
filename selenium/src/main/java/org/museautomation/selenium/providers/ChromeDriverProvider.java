package org.museautomation.selenium.providers;

import org.museautomation.builtins.network.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.util.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("chromedriver-provider")
public class ChromeDriverProvider extends BaseLocalDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        if (getOs() != null && !(OperatingSystem.get().equals(getOs())))
            return null;   // this provider is not for the current OS

        if (!capabilities.getName().equals(BrowserType.CHROME))
            return null;

        File path = getDriverLocation(context);
        if (path == null)
            {
            context.raiseEvent(MessageEventType.create("ChromeDriverProvider would try to satisfy request for Chrome browser, but it was not configured with a path to the driver"));
            return null;
            }

        if (!(path.exists()))
            {
            context.raiseEvent(MessageEventType.create("ChromeDriverProvider would try to satisfy request for Chrome browser, but the configured path does not exist: " + path.getAbsolutePath()));
            return null;
            }

        if (!(path.canExecute()))
            {
            boolean fixed = path.setExecutable(true);
            if (!fixed)
                {
                context.raiseEvent(MessageEventType.create("The ChromeDriver executable does not have execute permission and the user does not have permission to grant it. Driver file is: " + path.getAbsolutePath()));
                return null;
                }
            }

        synchronized (ChromeDriverProvider.class)
            {
            ChromeOptions options = new ChromeOptions();
            String[] arguments = resolveArguments(context);
            if (arguments.length > 0)
            	options.addArguments(arguments);

            DesiredCapabilities desired = DesiredCapabilities.chrome();
            if (capabilities.getVersion() != null && capabilities.getVersion().length() > 0)
                desired.setVersion(capabilities.getVersion());
            if (capabilities.getPlatform() != null && capabilities.getPlatform().length() > 0)
                desired.setPlatform(Platform.fromString(capabilities.getPlatform()));
            Proxy proxy = createProxy(context);
            if (proxy != null)
                desired.setCapability("proxy", proxy);
            options.merge(desired);

            System.setProperty("webdriver.chrome.driver", path.getAbsolutePath());
            return new ChromeDriver(options);
            }
        }

    private Proxy createProxy(MuseExecutionContext context)
        {
        NetworkProxyConfiguration config = getProxyConfigFromPlugin(context);
        if (config == null)
            return null;

        context.raiseEvent(MessageEventType.create("Use proxy: " + config));

        Proxy proxy = new Proxy();
        switch (config.getProxyType())
            {
            case None:
                proxy.setProxyType(Proxy.ProxyType.DIRECT);
                break;
            case System:
                proxy.setProxyType(Proxy.ProxyType.SYSTEM);
                break;
            case Fixed:
                proxy.setProxyType(Proxy.ProxyType.MANUAL);
                proxy.setHttpProxy(String.format("%s:%d", config.getHostname(), config.getPort()));
                proxy.setSslProxy(String.format("%s:%d", config.getHostname(), config.getPort()));
                break;
            case Script:
                proxy.setProxyType(Proxy.ProxyType.PAC);
                proxy.setProxyAutoconfigUrl(config.getPacUrl());
                break;
            }
        return proxy;
        }

    @Override
    public String getName()
        {
        return "ChromeDriver (local)";
        }

    @Override
    public String toString()
        {
        return "ChromeDriver";
        }
    }