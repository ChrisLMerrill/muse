package org.museautomation.selenium;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.openqa.selenium.*;

import java.util.*;

/**
 * A provider for a list of locally available WebDriver implementations.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("webdriver-provider-list")
public class WebDriverProviderList extends BaseMuseResource implements WebDriverProviderConfiguration
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
        if (_providers == null)
            _providers = new ArrayList<>();
        return _providers;
        }

    @SuppressWarnings("unused")  // used in UI
    public void add(WebDriverProvider provider)
        {
        int index = getProviders().size();
        getProviders().add(provider);
        for (WebDriverProviderList.ChangeListener listener : _listeners)
            listener.providerAdded(index, provider);
        }

    @SuppressWarnings("unused")  // used in UI
    public void add(int index, WebDriverProvider provider)
        {
        getProviders().add(index, provider);
        for (WebDriverProviderList.ChangeListener listener : _listeners)
            listener.providerAdded(index, provider);
        }

    public void remove(WebDriverProvider provider)
        {
        int index = getProviders().indexOf(provider);
        if (index >= 0)
            {
            getProviders().remove(index);
            for (WebDriverProviderList.ChangeListener listener : _listeners)
                listener.providerRemoved(index, provider);
            }
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
    public ResourceType getType()
        {
        return new WebdriverProviderResourceType();
        }

    public void addListener(ChangeListener listener)
        {
        _listeners.add(listener);
        }

    public void removeListener(ChangeListener listener)
        {
        _listeners.remove(listener);
        }

    private List<WebDriverProvider> _providers;

    private final transient Set<ChangeListener> _listeners = new HashSet<>();

    public final static String TYPE_ID = WebDriverProviderList.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class WebdriverProviderResourceType extends ResourceType
        {
        public WebdriverProviderResourceType()
            {
            super(TYPE_ID, "BrowserProvider", WebDriverProviderList.class);
            }
        }

    public interface ChangeListener
        {
        void providerAdded(int index, WebDriverProvider provider);
        void providerRemoved(int index, WebDriverProvider provider);
        }
    }


