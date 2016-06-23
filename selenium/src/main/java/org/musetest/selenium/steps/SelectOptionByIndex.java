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
@MuseTypeId("select-index")
@MuseStepName("Select by index")
@MuseInlineEditString("select choice #{index} in {element}")
@MuseStepIcon("glyph:FontAwesome:HAND_ALT_UP")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Select an option by index")
@MuseStepLongDescription("Resolves the 'element' source to a Select WebElement and then selects the option with index = 'index'.")
@MuseSubsourceDescriptor(displayName = "Element", description = "Locator for the element to select from", type = SubsourceDescriptor.Type.Named, name = SelectOptionByIndex.ELEMENT_PARAM)
@MuseSubsourceDescriptor(displayName = "Index", description = "Index of the entry to select (expects an integer)", type = SubsourceDescriptor.Type.Named, name = SelectOptionByIndex.INDEX_PARAM)
public class SelectOptionByIndex extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SelectOptionByIndex(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        _index_source = getValueSource(config, INDEX_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError
        {
        Long index = getValue(_index_source, context, false, Long.class);
        WebElement element = getElement(_element_source, context);
        try
            {
            Select select = new Select(element);
            select.selectByIndex(index.intValue());
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
    public final static String INDEX_PARAM = "index";

    public final static String TYPE_ID = SelectOptionByIndex.class.getAnnotation(MuseTypeId.class).value();
    }


