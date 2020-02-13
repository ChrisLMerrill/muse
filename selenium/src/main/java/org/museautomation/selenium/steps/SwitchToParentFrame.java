package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("switch-to-parent")
@MuseStepName("Switch To Parent Frame")
@MuseInlineEditString("switch to parent frame")
@MuseStepIcon("glyph:FontAwesome:EXCHANGE")
@MuseStepTypeGroup("Selenium.Frame")
@MuseStepShortDescription("Switch to the parent frame")
@MuseStepLongDescription("Switches the focus of the current driver to the parent of the currently-focused frame.")
@SuppressWarnings("unused") // instantiated via reflection
public class SwitchToParentFrame extends BrowserStep
    {
    public SwitchToParentFrame(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getDriver(context).switchTo().parentFrame();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = SwitchToParentFrame.class.getAnnotation(MuseTypeId.class).value();
    }