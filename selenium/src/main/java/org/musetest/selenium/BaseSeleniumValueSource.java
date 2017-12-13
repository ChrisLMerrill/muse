package org.musetest.selenium;

import org.musetest.core.*;
import org.musetest.core.values.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseSeleniumValueSource extends BaseValueSource
    {
    public BaseSeleniumValueSource(ValueSourceConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    protected WebDriver getDriver(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        return BrowserStepExecutionContext.getDriver(context);
        }
    }
