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
@MuseTypeId("enabled")
@MuseValueSourceName("Element is enabled")
@MuseValueSourceTypeGroup("Element.Condition")
@MuseValueSourceShortDescription("Returns true if the sub-source returns a Selenium WebElement that is enabled")
@MuseValueSourceLongDescription("Resolves the supplied element source. Returns true if it returns a Selenium WebElement and it is enabled.")
@MuseStringExpressionSupportImplementation(ElementEnabledConditionStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "True if the element supplied by the sub-source is enabled (expects a page element)", type = SubsourceDescriptor.Type.Single)
public class ElementEnabledCondition extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementEnabledCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, true);
        boolean enabled = element.isEnabled();
        context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), enabled));
        return enabled;
        }

    @Override
    public String getDescription()
        {
        return "enabled(" + getElementSource().getDescription() + ")";
        }

    public final static String TYPE_ID = ElementEnabledCondition.class.getAnnotation(MuseTypeId.class).value();
    }
