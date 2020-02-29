package org.museautomation.selenium.values;

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
@MuseTypeId("element-value")
@MuseValueSourceName("Element Value")
@MuseValueSourceTypeGroup("Selenium.Element.Value")
@MuseValueSourceShortDescription("Returns the value of the input element")
@MuseValueSourceLongDescription("Resolves the supplied element source, then returns the value the input element (if any).")
@MuseStringExpressionSupportImplementation(ElementValue.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to get value from", type = SubsourceDescriptor.Type.Single)
@SuppressWarnings("unused")  // instantiated via reflection
public class ElementValue extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementValue(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public String resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, true);
        String value = element.getAttribute("value");
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), value));
        return value;
        }

    @Override
    public String getDescription()
        {
        return "elementValue(" + getElementSource().getDescription() + ")";
        }

    public final static String TYPE_ID = ElementValue.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "elementValue";
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return ElementValue.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }
