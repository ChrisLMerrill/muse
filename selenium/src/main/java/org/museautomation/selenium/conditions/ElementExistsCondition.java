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
@MuseTypeId("exists")
@MuseValueSourceName("Element exists")
@MuseValueSourceTypeGroup("Selenium.Element.Condition")
@MuseValueSourceShortDescription("True if the element exists")
@MuseValueSourceLongDescription("Resolves the supplied element source. Returns true if it returns a Selenium WebElement, otherwise returns false.")
@MuseStringExpressionSupportImplementation(ElementExistsCondition.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to inspect", type = SubsourceDescriptor.Type.Single)
public class ElementExistsCondition extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementExistsCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, false);
        boolean exists = element != null;
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), exists));
        return exists;
        }

    @Override
    public String getDescription()
        {
        return "exists(" + getElementSource().getDescription() + ")";
        }

    public final static String TYPE_ID = ElementExistsCondition.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "elementExists";
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return ElementExistsCondition.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }
