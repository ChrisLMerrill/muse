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
@MuseTypeId("exists")
@MuseValueSourceName("Element exists")
@MuseValueSourceDescription("exists({source})")
@MuseValueSourceTypeGroup("Element")
public class ElementExistsCondition extends BrowserValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementExistsCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
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
        context.getTestExecutionContext().raiseEvent(new ValueSourceResolvedEvent(getDescription(), element));
        boolean exists = element instanceof WebElement;
        context.getTestExecutionContext().raiseEvent(new ValueSourceResolvedEvent(context.getTestExecutionContext().getProject().getValueSourceDescriptors().get(_configuration).getShortDescription(_configuration), exists));
        return exists;
        }

    @Override
    public String getDescription()
        {
        return "exists(" + _element_source.getDescription() + ")";
        }

    private ValueSourceConfiguration _configuration;
    private MuseValueSource _element_source;

    public final static String NAME = ElementExistsCondition.class.getAnnotation(MuseValueSourceDescription.class).value();
    public final static String TYPE_ID = ElementExistsCondition.class.getAnnotation(MuseTypeId.class).value();
    }
