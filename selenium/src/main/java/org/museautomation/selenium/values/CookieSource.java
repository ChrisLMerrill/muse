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
@MuseTypeId("get-cookie")
@MuseValueSourceName("Cookie")
@MuseValueSourceTypeGroup("Selenium.Cookie")
@MuseValueSourceShortDescription("Get a cookie")
@MuseValueSourceLongDescription("Returns a cookie, by name, or null if it does not exist.")
@MuseStringExpressionSupportImplementation(CookieSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Name", description = "The name of the cookie to get", name = CookieSource.NAME_PARAM, type = SubsourceDescriptor.Type.Named)
@SuppressWarnings("unused")  // instantiated by reflection
public class CookieSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public CookieSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _name_source = getValueSource(config, NAME_PARAM, true, getProject());
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String name = getValue(_name_source, context, false, String.class);
        Cookie cookie = getDriver(context).manage().getCookieNamed(name);
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), cookie));
        return cookie;
        }

    private final MuseValueSource _name_source;

    final static String NAME_PARAM = "name";

    public final static String TYPE_ID = CookieSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "cookie";
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {NAME_PARAM};
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return CookieSource.TYPE_ID;
            }
        }
    }
