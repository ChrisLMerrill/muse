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
@MuseTypeId("element-attribute")
@MuseValueSourceName("Element Attribute Value")
@MuseValueSourceTypeGroup("Selenium.Element.Value")
@MuseValueSourceShortDescription("Returns the value of the attribute on the element")
@MuseValueSourceLongDescription("Resolves the supplied element and attributes sources, then returns the attribute value the input element (if any).")
@MuseStringExpressionSupportImplementation(ElementAttributeValue.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to get value from", name = "element", type = SubsourceDescriptor.Type.Named)
@MuseSubsourceDescriptor(displayName = "Attribute", description = "The name of the attribute to get value from", name = ElementAttributeValue.ATTRIBUTE_NAME_PARAM, type = SubsourceDescriptor.Type.Named)
@SuppressWarnings("unused")  // instantiated via reflection
public class ElementAttributeValue extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementAttributeValue(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _attribute_name_source = getValueSource(config, ATTRIBUTE_NAME_PARAM, true, getProject());
        }

    protected MuseValueSource findElementSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        return getValueSource(config, ELEMENT_PARAM, true, getProject());
        }

    @Override
    public String resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, true);
        String name = getValue(_attribute_name_source, context, false, String.class);
        String value = element.getAttribute(name);
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), value));
        return value;
        }

    @Override
    public String getDescription()
        {
        return String.format("elementAttribute(%s,%s)", getElementSource().getDescription(), _attribute_name_source.getDescription());
        }

    private MuseValueSource _attribute_name_source;

    public final static String TYPE_ID = ElementAttributeValue.class.getAnnotation(MuseTypeId.class).value();
    public final static String ELEMENT_PARAM = "element";
    final static String ATTRIBUTE_NAME_PARAM = "attribute-name";

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "elementAttribute";
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {ELEMENT_PARAM, ATTRIBUTE_NAME_PARAM};
            }

        @Override
        protected int getNumberArguments()
            {
            return 2;
            }

        @Override
        protected String getTypeId()
            {
            return ElementAttributeValue.TYPE_ID;
            }
        }
    }
