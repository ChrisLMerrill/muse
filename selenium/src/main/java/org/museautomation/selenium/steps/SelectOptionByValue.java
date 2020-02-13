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
@MuseTypeId("select-value")
@MuseStepName("Select by option value")
@MuseInlineEditString("select choice with value {value} in {element}")
@MuseStepIcon("glyph:FontAwesome:HAND_ALT_UP")
@MuseStepTypeGroup("Selenium.Select")
@MuseStepShortDescription("Select an option by visible text")
@MuseStepLongDescription("Resolves the 'element' source to a Select WebElement and then selects the option with value = 'value'.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to select from", type = SubsourceDescriptor.Type.Named, name = SelectOptionByValue.ELEMENT_PARAM)
@MuseSubsourceDescriptor(displayName = "Value", description = "Value of the entry to select", type = SubsourceDescriptor.Type.Named, name = SelectOptionByValue.VALUE_PARAM)
public class SelectOptionByValue extends SelectOptionBaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SelectOptionByValue(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project, VALUE_PARAM);
        }

    @Override
    protected void executeSelection(Select select, String option, StepExecutionContext context)
        {
        select.selectByValue(option);
        }

    public final static String VALUE_PARAM = "value";

    public final static String TYPE_ID = SelectOptionByValue.class.getAnnotation(MuseTypeId.class).value();
    }