package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("switch-to-window")
@MuseStepName("Switch To Window")
@MuseInlineEditString("switch to {target}")
@MuseStepIcon("glyph:FontAwesome:EXCHANGE")
@MuseStepTypeGroup("Selenium.Window")
@MuseStepShortDescription("Switch the current driver target to a window/tab")
@MuseStepLongDescription("Resolves the 'target' source and calls the driver's switchTo() method with it. The parameter must be a browser window/tab handle (id).")
@MuseSubsourceDescriptor(displayName = "Target", description = "Id of the window/tab to switch to", type = SubsourceDescriptor.Type.Named, name = SwitchToWindow.TARGET_PARAM)
public class SwitchToWindow extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SwitchToWindow(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _target_source = getValueSource(config, TARGET_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        String target = getValue(_target_source, context, false, String.class);
        getDriver(context).switchTo().window(target);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _target_source;

    public final static String TARGET_PARAM = "target";

    public final static String TYPE_ID = SwitchToWindow.class.getAnnotation(MuseTypeId.class).value();
    }