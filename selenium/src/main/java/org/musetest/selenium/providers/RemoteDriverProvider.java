package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.net.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("remote-provider")
public class RemoteDriverProvider implements WebDriverProvider
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
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

    @SuppressWarnings("unused")  // used by JSON de/serialization and in UI
    public void setUrl(String url)
        {
        if (!Objects.equals(_url, url))
            {
            String old_url = _url;
            _url = url;
            for (ChangeListener listener : _listeners)
                listener.urlChanged(old_url, url);
            }
        }

    @Override
    public String toString()
        {
        return "remote (" + _url + ")";
        }

    @SuppressWarnings("unused")  // public API
    public void addChangeListener(ChangeListener listener)
        {
        _listeners.add(listener);
        }

    @SuppressWarnings("unused")  // public API
    public void removeChangeListener(ChangeListener listener)
        {
        _listeners.remove(listener);
        }

    private String _url;
    private transient Set<ChangeListener> _listeners = new HashSet<>();

    private final static Logger LOG = LoggerFactory.getLogger(RemoteDriverProvider.class);

    @SuppressWarnings("WeakerAccess")  // public API
    public interface ChangeListener
        {
        void urlChanged(String old_url, String new_url);
        }
    }


