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
@MuseTypeId("element-text")
@MuseValueSourceName("Element Text")
@MuseValueSourceTypeGroup("Selenium.Element.Value")
@MuseValueSourceShortDescription("Returns the text content of the sub-source Selenium WebElement")
@MuseValueSourceLongDescription("Resolves the supplied element source, then returns the text content of that element (if any).")
@MuseStringExpressionSupportImplementation(ElementTextSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to get text from", type = SubsourceDescriptor.Type.Single)
@MuseSubsourceDescriptor(displayName = "Return null on failure", description = "If false (default), failures encountered when accessing the element will be propogated outside of this source. When true, null will be returned when the value cannot be determined.", type = SubsourceDescriptor.Type.Named, name = ElementTextSource.NULL_ON_ERROR_PARAM, optional = true, defaultValue = "false")
@SuppressWarnings("unused")  // instantiated by reflection
public class ElementTextSource extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementTextSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _null_on_error_source = getValueSource(config, NULL_ON_ERROR_PARAM, false, project);
        }

    @Override
    public String resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Boolean null_on_failure = false;
        if (_null_on_error_source != null)
            null_on_failure = getValue(_null_on_error_source, context, Boolean.class, Boolean.FALSE);

        try
            {
            WebElement element = resolveElementSource(context, true);
            String text = element.getText();
            context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), text));
            return text;
            }
        catch (Exception e)
            {
            if (null_on_failure)
                return null;
            else
                throw e;
            }
        }

    @Override
    public String getDescription()
        {
        return "elementText(" + getElementSource().getDescription() + ")";
        }

    private final MuseValueSource _null_on_error_source;

    public final static String TYPE_ID = ElementTextSource.class.getAnnotation(MuseTypeId.class).value();
    public final static String NULL_ON_ERROR_PARAM = "null_on_failure";

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "elementText";
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return ElementTextSource.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }
