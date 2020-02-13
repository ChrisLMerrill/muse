package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("back")
@MuseStepName("Back")
@MuseInlineEditString("Back")
@MuseStepIcon("glyph:FontAwesome:ARROW_LEFT")
@MuseStepTypeGroup("Selenium.Navigate")
@MuseStepShortDescription("Go Back one page.")
@MuseStepLongDescription("Navigate the browser to the previous page (i.e. press the back button). The browser must have previously navigated to a page using the Goto URL step. If not, this operation will probably do nothing (the behavior is browser dependent).")
public class NavigateBack extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public NavigateBack(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getDriver(context).navigate().back();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = NavigateBack.class.getAnnotation(MuseTypeId.class).value();
    }


