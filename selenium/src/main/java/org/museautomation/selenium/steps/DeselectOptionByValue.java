package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;
import org.openqa.selenium.support.ui.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("deselect-value")
@MuseStepName("Deselect by option value")
@MuseInlineEditString("deselect choice with value {value} in {element}")
@MuseStepIcon("glyph:FontAwesome:HAND_ALT_UP")
@MuseStepTypeGroup("Selenium.Select")
@MuseStepShortDescription("Deselect an option by visible text")
@MuseStepLongDescription("Resolves the 'element' source to a Select WebElement and then deselects the option with value = 'value'.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to deselect from", type = SubsourceDescriptor.Type.Named, name = DeselectOptionByValue.ELEMENT_PARAM)
@MuseSubsourceDescriptor(displayName = "Value", description = "Value of the entry to deselect", type = SubsourceDescriptor.Type.Named, name = DeselectOptionByValue.VALUE_PARAM)
public class DeselectOptionByValue extends SelectOptionBaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public DeselectOptionByValue(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project, VALUE_PARAM);
        }

    @Override
    protected void executeSelection(Select select, String option, StepExecutionContext context)
        {
        select.deselectByValue(option);
        }

    public final static String VALUE_PARAM = "value";

    public final static String TYPE_ID = DeselectOptionByValue.class.getAnnotation(MuseTypeId.class).value();
    }