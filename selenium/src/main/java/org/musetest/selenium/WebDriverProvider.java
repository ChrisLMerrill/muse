package org.musetest.selenium;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface WebDriverProvider
    {
    WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context);

    String TYPE_ID = SeleniumBrowserCapabilities.class.getAnnotation(MuseTypeId.class).value();
    }


