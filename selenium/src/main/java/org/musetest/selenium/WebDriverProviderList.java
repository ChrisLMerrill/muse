package org.musetest.selenium;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.openqa.selenium.*;

import java.util.*;

/**
 * A provider for a list of locally available WebDriver implementations.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("webdriver-provider-list")
public class WebDriverProviderList implements WebDriverProviderConfiguration
    {
    @Override
    public WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context)
        {
        for (WebDriverProvider provider : _providers)
            {
            WebDriver driver = provider.getDriver(capabilities, context);
            if (driver != null)
                return driver;
            }

        return null;
        }

    @SuppressWarnings("unused")  // JSON de/serialization
    public List<WebDriverProvider> getProviders()
        {
        return _providers;
        }

    @SuppressWarnings("unused")  // JSON de/serialization
    public void setProviders(List<WebDriverProvider> providers)
        {
        _providers = providers;
        }

    @Override
    public String toString()
        {
        StringBuilder builder = new StringBuilder("providers[");
        boolean first = true;
        for (WebDriverProvider provider : _providers)
            {
            if (first)
                first = false;
            else
                builder.append(",");
            builder.append(provider);
            }
        builder.append("]");
        return builder.toString();
        }

    @Override
    public ResourceMetadata getMetadata()
        {
        return _metadata;
        }

    private ResourceMetadata _metadata = new ResourceMetadata();

    private List<WebDriverProvider> _providers;

    public final static String TYPE_ID = WebDriverProviderList.class.getAnnotation(MuseTypeId.class).value();
    }


