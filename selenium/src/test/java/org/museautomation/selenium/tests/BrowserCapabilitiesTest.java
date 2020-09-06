package org.museautomation.selenium.tests;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.resource.json.*;
import org.museautomation.core.util.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class BrowserCapabilitiesTest
    {
    @Test
    void createCapabilities() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMuseTypeMapper(new TypeLocator((MuseProject)null));
        SeleniumBrowserCapabilities sel_caps = mapper.readValue(getClass().getResourceAsStream("capabilities1.json"), SeleniumBrowserCapabilities.class);

        DesiredCapabilities capabilities = sel_caps.toDesiredCapabilities();

        Assertions.assertEquals(BrowserType.FIREFOX, capabilities.getBrowserName());
        Assertions.assertEquals(Platform.LINUX, capabilities.getPlatform());
        Assertions.assertEquals("7.1", capabilities.getVersion());
        }
    }


