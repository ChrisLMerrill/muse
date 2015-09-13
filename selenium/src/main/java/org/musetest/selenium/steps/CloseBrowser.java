package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("close-browser")
@MuseStepName("Close Browser")
@MuseStepShortDescription("Close a browser and all associated windows")
@MuseInlineEditString("close browser")
@MuseStepIcon("glyph:FontAwesome:GLOBE")
@MuseStepTypeGroup("Selenium")
public class CloseBrowser extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public CloseBrowser(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        }

    @Override
    public StepExecutionResult execute(StepExecutionContext context) throws StepConfigurationError
        {
        getDriver(context).quit();
        BrowserStepExecutionContext.putDriver(null, context);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = CloseBrowser.class.getAnnotation(MuseTypeId.class).value();
    }


