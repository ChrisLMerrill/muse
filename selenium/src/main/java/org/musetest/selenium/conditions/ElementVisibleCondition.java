package org.musetest.selenium.conditions;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
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
@MuseValueSourceLongDescription("Resolves the supplied element source. Returns true if it returns a Selenium WebElement and it is visible.")
@MuseStringExpressionSupportImplementation(ElementVisibleConditionStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to inspect", type = SubsourceDescriptor.Type.Single)
public class ElementVisibleCondition extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementVisibleCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, true);
        boolean visible = element.isDisplayed();
        context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), visible));
        return visible;
        }

    @Override
    public String getDescription()
        {
        return "visible(" + getElementSource().getDescription() + ")";
        }

    public final static String TYPE_ID = ElementVisibleCondition.class.getAnnotation(MuseTypeId.class).value();
    }
