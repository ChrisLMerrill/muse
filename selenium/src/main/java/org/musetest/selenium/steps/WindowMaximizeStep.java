package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("browser-maximize")
@MuseStepName("Maximize")
@MuseInlineEditString("Maximize browser window")
@MuseStepIcon("glyph:FontAwesome:WINDOW_MAXIMIZE")
@MuseStepTypeGroup("Selenium.Window")
@MuseStepShortDescription("Maximize browser window")
@MuseStepLongDescription("Change browser window size to the size of the screen.")
@SuppressWarnings("unused,WeakerAccess")
public class WindowMaximizeStep extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public WindowMaximizeStep(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getDriver(context).manage().window().maximize();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = WindowMaximizeStep.class.getAnnotation(MuseTypeId.class).value();
    }