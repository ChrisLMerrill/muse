package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("browser-fullscreen")
@MuseStepName("Full-screen")
@MuseInlineEditString("Enter full-screen mode")
@MuseStepIcon("glyph:FontAwesome:SQUARE_ALT")
@MuseStepTypeGroup("Selenium.Window")
@MuseStepShortDescription("Enter full-screen mode")
@MuseStepLongDescription("Switch browser into full-screen mode.")
@SuppressWarnings("unused,WeakerAccess")
public class WindowFullscreenStep extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public WindowFullscreenStep(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getDriver(context).manage().window().fullscreen();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = WindowFullscreenStep.class.getAnnotation(MuseTypeId.class).value();
    }