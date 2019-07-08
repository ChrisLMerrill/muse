package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.support.ui.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("deselect-text")
@MuseStepName("Deselect by visible text")
@MuseInlineEditString("deselect choice labeled {text} in {element}")
@MuseStepIcon("glyph:FontAwesome:HAND_ALT_UP")
@MuseStepTypeGroup("Selenium.select")
@MuseStepShortDescription("Deselect an option by visible text")
@MuseStepLongDescription("Resolves the 'element' source to a Select WebElement and then deselects the option with visible text = 'text'.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to deselect from", type = SubsourceDescriptor.Type.Named, name = DeselectOptionByText.ELEMENT_PARAM)
@MuseSubsourceDescriptor(displayName = "Text", description = "Text of the entry to deselect", type = SubsourceDescriptor.Type.Named, name = DeselectOptionByText.TEXT_PARAM)
public class DeselectOptionByText extends SelectOptionBaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public DeselectOptionByText(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project, TEXT_PARAM);
        }

    @Override
    protected void executeSelection(Select select, String option, StepExecutionContext context)
        {
        select.deselectByVisibleText(option);
        }

    public final static String TEXT_PARAM = "text";

    public final static String TYPE_ID = DeselectOptionByText.class.getAnnotation(MuseTypeId.class).value();
    }