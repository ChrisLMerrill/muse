package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("close-window")
@MuseStepName("Close Window")
@MuseInlineEditString("Close current window")
@MuseStepIcon("glyph:FontAwesome:WINDOW_CLOSE")
@MuseStepTypeGroup("Selenium.Window")
@MuseStepShortDescription("Close the current browser tab and/or window")
@MuseStepLongDescription("Closes the current browser tab or window. If it is the last tab/window opened, it will shutdown the browser.")
public class CloseWindow extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public CloseWindow(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getDriver(context).close();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = CloseWindow.class.getAnnotation(MuseTypeId.class).value();
    }