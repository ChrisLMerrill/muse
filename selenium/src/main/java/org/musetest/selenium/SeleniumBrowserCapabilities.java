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
public class SeleniumBrowserCapabilities implements MuseResource
    {
    public SeleniumBrowserCapabilities()
        {
        }

    public SeleniumBrowserCapabilities(String browser)
        {
        _capabilities.put(BROWSER_NAME, browser);
        }

    @Override
    public ResourceMetadata getMetadata()
        {
        return _metadata;
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
        StringBuilder builder = new StringBuilder(getMetadata().getId());
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

    private Map<String, Object> _capabilities = new HashMap<>();

    private ResourceMetadata _metadata = new ResourceMetadata();

    public final static String BROWSER_NAME = "browserName";
    public final static String TYPE_ID = SeleniumBrowserCapabilities.class.getAnnotation(MuseTypeId.class).value();
    }


