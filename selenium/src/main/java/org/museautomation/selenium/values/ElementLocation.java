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
@MuseTypeId("element-location")
@MuseValueSourceName("Element Location")
@MuseValueSourceTypeGroup("Selenium.Element.Location")
@MuseValueSourceShortDescription("Returns the location of the element (x,y)")
@MuseValueSourceLongDescription("Resolves the supplied element source, then returns the location the input element.")
@MuseStringExpressionSupportImplementation(ElementLocation.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to get location for", type = SubsourceDescriptor.Type.Single)
@SuppressWarnings("unused")  // instantiated via reflection
public class ElementLocation extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementLocation(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Point resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, true);
        Point location = element.getLocation();
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), location.toString()));
        return location;
        }

    @Override
    public String getDescription()
        {
        return "elementLocation(" + getElementSource().getDescription() + ")";
        }

    public final static String TYPE_ID = ElementLocation.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "elementLocation";
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return ElementLocation.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }
