package org.musetest.selenium.tests;

import com.fasterxml.jackson.databind.*;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
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

        Assertions.assertEquals(BrowserType.FIREFOX, capabilities.getBrowserName());
        Assertions.assertEquals(Platform.LINUX, capabilities.getPlatform());
        Assertions.assertEquals("7.1", capabilities.getVersion());
        }
    }


