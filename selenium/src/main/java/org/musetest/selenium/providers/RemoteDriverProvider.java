package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("remote-provider")
public class RemoteDriverProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities)
        {
        synchronized (RemoteDriverProvider.class)
            {
            try
                {
                return new RemoteWebDriver(new URL(_url), capabilities.toDesiredCapabilities());
                }
            catch (MalformedURLException e)
                {
                LOG.error("Unable to parse URL: " + _url);
                }
            }
        return null;
        }

    public String getUrl()
        {
        return _url;
        }

    @SuppressWarnings("unused")  // used by JSON de/serialization
    public void setUrl(String url)
        {
        _url = url;
        }

    private String _url;

    final static Logger LOG = LoggerFactory.getLogger(RemoteDriverProvider.class);
    }


