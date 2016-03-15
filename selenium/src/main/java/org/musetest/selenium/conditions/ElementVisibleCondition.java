package org.musetest.selenium.conditions;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("visible")
@MuseValueSourceName("Element is visible")
@MuseValueSourceTypeGroup("Element.Condition")
@MuseValueSourceShortDescription("Returns true if the sub-source returns a Selenium WebElement that is visible")
@MuseValueSourceLongDescription("Resolves the supplied element source. Returns true if it returns a Selenium WebElement, otherwise returns false.")
@MuseStringExpressionSupportImplementation(ElementVisibleConditionStringExpressionSupport.class)
public class ElementVisibleCondition extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementVisibleCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        ValueSourceConfiguration source = config.getSource();
        if (source == null)
            throw new MuseInstantiationException("ElementExistsValueSource requires a source for the element.");
        _element_source = source.createSource(project);
        }

    @Override
    public Boolean resolveValue(StepExecutionContext context) throws StepConfigurationError
        {
        Object element = _element_source.resolveValue(context);
        if (element == null)
            throw new ValueSourceResolutionError("Cannot determine visibility of element: element not found");
        if (!(element instanceof WebElement))
            throw new ValueSourceResolutionError("The sub-source result should be a WebElement. Did not expect a " + element.getClass().getSimpleName());
        boolean visible = ((WebElement) element).isDisplayed();
        context.getTestExecutionContext().raiseEvent(new ValueSourceResolvedEvent(getDescription(), visible));
        return visible;
        }

    @Override
    public String getDescription()
        {
        return "visible(" + _element_source.getDescription() + ")";
        }

    private MuseValueSource _element_source;

    public final static String TYPE_ID = ElementVisibleCondition.class.getAnnotation(MuseTypeId.class).value();
    }
