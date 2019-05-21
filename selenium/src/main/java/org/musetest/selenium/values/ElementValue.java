package org.musetest.selenium.values;

import org.musetest.builtins.value.*;
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
@MuseTypeId("element-value")
@MuseValueSourceName("Element Value")
@MuseValueSourceTypeGroup("Element.Value")
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
