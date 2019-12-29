package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("refresh")
@MuseStepName("Refresh")
@MuseInlineEditString("Refresh")
@MuseStepIcon("glyph:FontAwesome:REDO")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Refresh the page.")
@MuseStepLongDescription("Refresh the current page by re-loading content from the server.")
public class RefreshStep extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public RefreshStep(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getDriver(context).navigate().refresh();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = RefreshStep.class.getAnnotation(MuseTypeId.class).value();
    }


