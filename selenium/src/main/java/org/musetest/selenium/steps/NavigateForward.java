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
@MuseTypeId("forward")
@MuseStepName("Forward")
@MuseInlineEditString("Forward")
@MuseStepIcon("glyph:FontAwesome:ARROW_RIGHT")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Go forward one page.")
@MuseStepLongDescription("Advance the browser to the next page (i.e. press the forward button). The browser must have previously navigated to a previous page using the back step. If not, this operation will probably do nothing (the behavior is browser dependent).")
@SuppressWarnings("WeakerAccess,unused") // invoked via reflection
public class NavigateForward extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public NavigateForward(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getDriver(context).navigate().forward();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = NavigateForward.class.getAnnotation(MuseTypeId.class).value();
    }


