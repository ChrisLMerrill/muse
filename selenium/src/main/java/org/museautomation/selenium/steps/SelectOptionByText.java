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
@MuseTypeId("select-text")
@MuseStepName("Select by visible text")
@MuseInlineEditString("select choice labeled {text} in {element}")
@MuseStepIcon("glyph:FontAwesome:HAND_ALT_UP")
@MuseStepTypeGroup("Selenium.Select")
@MuseStepShortDescription("Select an option by visible text")
@MuseStepLongDescription("Resolves the 'element' source to a Select WebElement and then selects the option with visible text = 'text'.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to select from", type = SubsourceDescriptor.Type.Named, name = SelectOptionByText.ELEMENT_PARAM)
@MuseSubsourceDescriptor(displayName = "Text", description = "Text of the entry to select", type = SubsourceDescriptor.Type.Named, name = SelectOptionByText.TEXT_PARAM)
public class SelectOptionByText extends SelectOptionBaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SelectOptionByText(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project, TEXT_PARAM);
        }

    @Override
    protected void executeSelection(Select select, String option, StepExecutionContext context)
        {
        select.selectByVisibleText(option);
        }

    public final static String TEXT_PARAM = "text";

    public final static String TYPE_ID = SelectOptionByText.class.getAnnotation(MuseTypeId.class).value();
    }