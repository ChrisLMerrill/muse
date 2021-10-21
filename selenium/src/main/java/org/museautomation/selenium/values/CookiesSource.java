package org.museautomation.selenium.values;

import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("get-cookies")
@MuseValueSourceName("Cookie List")
@MuseValueSourceTypeGroup("Selenium.Cookie")
@MuseValueSourceShortDescription("Get all cookies")
@MuseValueSourceLongDescription("Returns a list of all cookies for the domain of the current page")
@MuseStringExpressionSupportImplementation(CookiesSource.StringExpressionSupport.class)
@SuppressWarnings("unused")  // instantiated by reflection
public class CookiesSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public CookiesSource(ValueSourceConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Set<Cookie> set = getDriver(context).manage().getCookies();
        List<Cookie> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(set.size());
        builder.append(" cookies: ");
        int cookies_listed = 0;
        for (Cookie cookie : set)
            {
            list.add(cookie);

            if (cookies_listed > 0)
                builder.append(", ");
            builder.append(cookie.getName());
            builder.append("=");
            String value = cookie.getValue();
            if (value.length() > 50)
                value = value.substring(0, 50) + "...";
            builder.append(value);
            cookies_listed++;
            }
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), builder.toString()));
        return list;
        }

    public final static String TYPE_ID = CookiesSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "listCookies";
            }

        @Override
        protected int getNumberArguments()
            {
            return 0;
            }

        @Override
        protected String getTypeId()
            {
            return CookiesSource.TYPE_ID;
            }
        }
    }
