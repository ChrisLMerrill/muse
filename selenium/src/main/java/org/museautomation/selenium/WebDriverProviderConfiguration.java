package org.museautomation.selenium;

import org.museautomation.core.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface WebDriverProviderConfiguration extends MuseResource
    {
    WebDriver getDriver(SeleniumBrowserCapabilities capabilities, MuseExecutionContext context);
    }


