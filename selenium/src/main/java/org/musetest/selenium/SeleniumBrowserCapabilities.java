package org.musetest.selenium;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.openqa.selenium.remote.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("selenium-capabilities")
public class SeleniumBrowserCapabilities extends BaseMuseResource
    {
    public SeleniumBrowserCapabilities()
        {
        }

    public SeleniumBrowserCapabilities(String browser)
        {
        _capabilities.put(CapabilityType.BROWSER_NAME, browser);
        }

    @Override
    public ResourceType getType()
        {
        return new BrowserResourceType();
        }

    public String getName()
        {
        return _capabilities.get(CapabilityType.BROWSER_NAME).toString();
        }

    public void setName(String name)
        {
        setCapability(CapabilityType.BROWSER_NAME, name);
        }

    @SuppressWarnings("unused") // used in UI
    public String getPlatform()
        {
        return _capabilities.get(CapabilityType.PLATFORM).toString();
        }

    @SuppressWarnings("unused") // used in UI
    public void setPlatform(String platform)
        {
        setCapability(CapabilityType.PLATFORM, platform);
        }

    @SuppressWarnings("unused") // used in UI
    public String getVersion()
        {
        return _capabilities.get(CapabilityType.VERSION).toString();
        }

    @SuppressWarnings("unused") // used in UI
    public void setVersion(String version)
        {
        setCapability(CapabilityType.VERSION, version);
        }

    private void setCapability(String name, Object new_value)
        {
        Object old_value = _capabilities.get(name);
        _capabilities.put(name, new_value);
        for (ChangeListener listener : _listeners)
            listener.capabilityChanged(name, old_value, new_value);
        }

    public DesiredCapabilities toDesiredCapabilities()
        {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (_capabilities != null)
            for (String name : _capabilities.keySet())
                capabilities.setCapability(name, _capabilities.get(name));
        return capabilities;
        }

    @SuppressWarnings("unused")  // required for Jackson de/serialization
    public Map<String, Object> getCapabilities()
        {
        return _capabilities;
        }

    @SuppressWarnings("unused")  // required for Jackson de/serialization
    public void setCapabilities(Map<String, Object> capabilities)
        {
        _capabilities = capabilities;
        }

    @Override
    public String toString()
        {
        StringBuilder builder = new StringBuilder(getId());
        builder.append("[");
        boolean first = true;
        for (String key : _capabilities.keySet())
            {
            if (first)
                first = false;
            else
                builder.append(", ");

            builder.append(key);
            builder.append("=");
            Object value = _capabilities.get(key);
            if (value != null)
                value = value.toString();
            builder.append(value);
            }
        builder.append("]");
        return builder.toString();
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

    private Map<String, Object> _capabilities = new HashMap<>();
    private transient Set<ChangeListener> _listeners = new HashSet<>();

    public final static String TYPE_ID = SeleniumBrowserCapabilities.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class BrowserResourceType extends ResourceType
        {
        public BrowserResourceType()
            {
            super(TYPE_ID, "Browser", SeleniumBrowserCapabilities.class);
            }
        }

    @SuppressWarnings("WeakerAccess")  // public API
    public interface ChangeListener
        {
        void capabilityChanged(String name, Object old_value, Object new_value);
        }

    }


