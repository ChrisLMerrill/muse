package org.musetest.selenium.tests;

import com.fasterxml.jackson.databind.*;
import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.resource.json.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BrowserCapabilitiesTest
    {
    @Test
    public void createCapabilities() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        SeleniumBrowserCapabilities sel_caps = mapper.readValue(getClass().getResourceAsStream("capabilities1.json"), SeleniumBrowserCapabilities.class);

        DesiredCapabilities capabilities = sel_caps.toDesiredCapabilities();

        Assert.assertEquals(BrowserType.FIREFOX, capabilities.getBrowserName());
        Assert.assertEquals(Platform.LINUX, capabilities.getPlatform());
        Assert.assertEquals("7.1", capabilities.getVersion());
        }
    }


