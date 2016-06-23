package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("select-text")
@MuseStepName("Select by visible text")
@MuseInlineEditString("select choice {text} in {element}")
@MuseStepIcon("glyph:FontAwesome:HAND_ALT_UP")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Select an option by visible text")
@MuseStepLongDescription("Resolves the 'element' source to a Select WebElement and then selects the option with visible text = 'text'.")
@MuseSubsourceDescriptor(displayName = "Element", description = "Locator for the element to select from", type = SubsourceDescriptor.Type.Named, name = SelectOptionByText.ELEMENT_PARAM)
@MuseSubsourceDescriptor(displayName = "Text", description = "Text of the entry to select", type = SubsourceDescriptor.Type.Named, name = SelectOptionByText.TEXT_PARAM)
public class SelectOptionByText extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SelectOptionByText(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        _index_source = getValueSource(config, TEXT_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError
        {
        String text = getValue(_index_source, context, false, String.class);
        WebElement element = getElement(_element_source, context);
        try
            {
            Select select = new Select(element);
            select.selectByVisibleText(text);
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            }
        catch (UnexpectedTagNameException e)
            {
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "The element is not a <select> tag.");
            }
        }

    private MuseValueSource _element_source;
    private MuseValueSource _index_source;

    public final static String ELEMENT_PARAM = "element";
    public final static String TEXT_PARAM = "text";

    public final static String TYPE_ID = SelectOptionByText.class.getAnnotation(MuseTypeId.class).value();
    }


