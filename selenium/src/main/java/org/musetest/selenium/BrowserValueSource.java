package org.musetest.selenium;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;
import org.musetest.selenium.steps.*;
import org.openqa.selenium.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BrowserValueSource extends BaseValueSource
    {
    public BrowserValueSource(ValueSourceConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    protected WebDriver getDriver(StepExecutionContext context) throws ValueSourceResolutionError
        {
        return BrowserStepExecutionContext.getDriver(context);
        }
    }
