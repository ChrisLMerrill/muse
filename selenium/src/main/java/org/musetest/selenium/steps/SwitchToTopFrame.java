package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("switch-to-top")
@MuseStepName("Switch To Top Frame")
@MuseStepIcon("glyph:FontAwesome:EXCHANGE")
@MuseStepTypeGroup("Selenium.Frame")
@MuseStepShortDescription("Switch to the top frame")
@MuseStepLongDescription("Switches the focus of the current driver to the top-level context of the page/document.")
@SuppressWarnings("unused") // instantiated via reflection
public class SwitchToTopFrame extends BrowserStep
    {
    public SwitchToTopFrame(StepConfiguration config, MuseProject project)
        {
        super(config);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getDriver(context).switchTo().defaultContent();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String TYPE_ID = SwitchToTopFrame.class.getAnnotation(MuseTypeId.class).value();
    }