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
@MuseTypeId("element-text")
@MuseValueSourceName("Element Text")
@MuseValueSourceTypeGroup("Element.Value")
@MuseValueSourceShortDescription("Returns the text content of the sub-source Selenium WebElement")
@MuseValueSourceLongDescription("Resolves the supplied element source, then returns the text content of that element (if any).")
@MuseStringExpressionSupportImplementation(ElementText.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to get text from", type = SubsourceDescriptor.Type.Single)
public class ElementText extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementText(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public String resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, true);
        String text = element.getText();
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), text));
        return text;
        }

    @Override
    public String getDescription()
        {
        return "elementText(" + getElementSource().getDescription() + ")";
        }

    public final static String TYPE_ID = ElementText.class.getAnnotation(MuseTypeId.class).value();

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
            return ElementText.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }
