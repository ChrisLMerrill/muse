package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("close-browser")
@MuseStepName("Close Browser")
@MuseInlineEditString("close browser")
@MuseStepIcon("glyph:FontAwesome:GLOBE")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Close a browser and all associated windows")
@MuseStepLongDescription("Closes the currently-selected browser by calling driver.quit(). This will close all browser windows associated with that driver and shutdown the driver.")
public class CloseBrowser extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public CloseBrowser(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        getDriver(context).quit();
        BrowserStepExecutionContext.putDriver(null, context);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = CloseBrowser.class.getAnnotation(MuseTypeId.class).value();
    }


