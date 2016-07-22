package org.musetest.selenium.conditions;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("exists")
@MuseValueSourceName("Element exists")
@MuseValueSourceTypeGroup("Element.Condition")
@MuseValueSourceShortDescription("True if the element exists")
@MuseValueSourceLongDescription("Resolves the supplied element source. Returns true if it returns a Selenium WebElement, otherwise returns false.")
@MuseStringExpressionSupportImplementation(ElementExistsConditionStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to inspect", type = SubsourceDescriptor.Type.Single)
public class ElementExistsCondition extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementExistsCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Boolean resolveValue(StepExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, false);
        boolean exists = element != null;
        context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), exists));
        return exists;
        }

    @Override
    public String getDescription()
        {
        return "exists(" + getElementSource().getDescription() + ")";
        }

    public final static String TYPE_ID = ElementExistsCondition.class.getAnnotation(MuseTypeId.class).value();
    }
