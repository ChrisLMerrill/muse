package org.musetest.selenium.conditions;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.values.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("visible")
@MuseValueSourceName("Element is visible")
@MuseValueSourceInstanceDescription("visible({source})")
@MuseValueSourceTypeGroup("Element")
@MuseValueSourceShortDescription("Returns true if the sub-source returns a Selenium WebElement that is visible")
@MuseValueSourceLongDescription("Resolves the supplied element source. Returns true if it returns a Selenium WebElement, otherwise returns false.")
public class ElementVisibleCondition extends BrowserValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementVisibleCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        _configuration = config;
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
            throw new ValueSourceResolutionError("The specificed object is not a WebElement.");
        boolean visible = ((WebElement) element).isDisplayed();
        context.getTestExecutionContext().raiseEvent(new ValueSourceResolvedEvent(context.getTestExecutionContext().getProject().getValueSourceDescriptors().get(_configuration).getInstanceDescription(_configuration), visible));
        return visible;
        }

    @Override
    public String getDescription()
        {
        return "visible(" + _element_source.getDescription() + ")";
        }

    private ValueSourceConfiguration _configuration;
    private MuseValueSource _element_source;

    public final static String NAME = ElementVisibleCondition.class.getAnnotation(MuseValueSourceInstanceDescription.class).value();
    public final static String TYPE_ID = ElementVisibleCondition.class.getAnnotation(MuseTypeId.class).value();
    }
