package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("switch-to")
@MuseStepName("Switch To")
@MuseInlineEditString("switch to {target}")
@MuseStepIcon("glyph:FontAwesome:EXCHANGE")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Switch the current driver target")
@MuseStepLongDescription("Resolves the 'target' source and calls the driver's switchTo() method with it. The parameter can be an element (name, window and alerts not yet supported).")
@MuseSubsourceDescriptor(displayName = "Target", description = "Locator for the element to switch to", type = SubsourceDescriptor.Type.Named, name = SwitchTo.TARTGET_PARAM)
public class SwitchTo extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SwitchTo(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _target_source = getValueSource(config, TARTGET_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError
        {
        Object target = getValue(_target_source, context, false, Object.class);
        if (target instanceof WebElement)
            getDriver(context).switchTo().frame((WebElement)target);
        else
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, String.format("Unable to switch to target because type of target is not recognized: %s = %s", target.getClass().getSimpleName(), target,toString()));
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _target_source;

    public final static String TARTGET_PARAM = "target";

    public final static String TYPE_ID = SwitchTo.class.getAnnotation(MuseTypeId.class).value();
    }


