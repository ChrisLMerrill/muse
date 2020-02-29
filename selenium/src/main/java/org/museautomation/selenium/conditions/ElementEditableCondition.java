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
@MuseTypeId("editable")
@MuseValueSourceName("Element is editable")
@MuseValueSourceTypeGroup("Selenium.Element.Condition")
@MuseValueSourceShortDescription("Returns true if the sub-source returns a Selenium WebElement that is does not have the readonly attribute")
@MuseValueSourceLongDescription("Resolves the supplied element source. Returns true if it returns a Selenium WebElement and that does not have the readonly.")
@MuseStringExpressionSupportImplementation(ElementEditableCondition.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "True if the element supplied by the sub-source is editable", type = SubsourceDescriptor.Type.Single)
@SuppressWarnings("unused") // instantiated via reflection
public class ElementEditableCondition extends BaseElementValueSource
    {
    public ElementEditableCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, true);
        boolean editable = element.getAttribute("readonly") == null;
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), editable));
        return editable;
        }

    @Override
    public String getDescription()
        {
        return "elementEditable(" + getElementSource().getDescription() + ")";
        }

    public final static String TYPE_ID = ElementEditableCondition.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "elementEditable";
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return ElementEditableCondition.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }