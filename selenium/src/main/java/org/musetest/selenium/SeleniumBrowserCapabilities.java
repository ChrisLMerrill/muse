package org.musetest.selenium;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.javascript.factory.*;
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

    private Map<String, Object> _capabilities = new HashMap<>();

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

    private ResourceMetadata _metadata = new ResourceMetadata();

    public final static String BROWSER_NAME = "browserName";
    }


