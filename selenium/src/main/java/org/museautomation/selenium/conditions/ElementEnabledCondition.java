package org.museautomation.selenium.conditions;

import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("enabled")
@MuseValueSourceName("Element is enabled")
@MuseValueSourceTypeGroup("Element.Condition")
@MuseValueSourceShortDescription("Returns true if the sub-source returns a Selenium WebElement that is enabled")
@MuseValueSourceLongDescription("Resolves the supplied element source. Returns true if it returns a Selenium WebElement and it is enabled.")
@MuseStringExpressionSupportImplementation(ElementEnabledCondition.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "True if the element supplied by the sub-source is enabled", type = SubsourceDescriptor.Type.Single)
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
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), enabled));
        return enabled;
        }

    @Override
    public String getDescription()
        {
        return "elementEnabled(" + getElementSource().getDescription() + ")";
        }

    public final static String TYPE_ID = ElementEnabledCondition.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "elementEnabled";
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return ElementEnabledCondition.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }