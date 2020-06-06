package org.museautomation.selenium.locators;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.openqa.selenium.*;

import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("element-parameterized-xpath")
@MuseValueSourceName("Element by Parameterized XPath")
@MuseValueSourceTypeGroup("Selenium.Element.Locate")
@MuseValueSourceShortDescription("Locate an element with a Parameterized XPath locator")
@MuseValueSourceLongDescription("Locate a WebElement using a parameterized XPath expression. The .")
@MuseStringExpressionSupportImplementation(ParameterizedXPathElementValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "XPath id", description = "The ID of the parameterized XPath (a project resource)", type = SubsourceDescriptor.Type.Single)
@MuseSubsourceDescriptor(displayName = "Parameter(s)", description = "Parameters to pass to the XPath. Must resolve to a String or a List of Strings.", name = ParameterizedXPathElementValueSource.PARAMS_PARAM, type = SubsourceDescriptor.Type.Named)
@MuseSubsourceDescriptor(displayName = "Find Multiple", description = "If true, look for 0 to many matching elements and resolve as a list. If false (default) return first match and fail if none found", type = SubsourceDescriptor.Type.Named, name = ElementByLocatorValueSource.MULTIPLE_PARAM, defaultValue = "false", optional = true)
public class ParameterizedXPathElementValueSource extends ElementByLocatorValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ParameterizedXPathElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _params_source = getValueSource(config, PARAMS_PARAM, true, project);
        }

    /**
     * In this sub-class, the locator_string (named locator_id here) is not the locator string - it is the ID
     * of the ParameteriziedXPathLocator. That is a project resource that is fetched and then used to create an
     * XPath.
     */
    protected By createBy(MuseExecutionContext context, String locator_id) throws ValueSourceResolutionError
        {
        ParameterizedXPathLocatorConfiguration resource = context.getProject().getResourceStorage().getResource(locator_id, ParameterizedXPathLocatorConfiguration.class);
        String pattern;
        try
            {
            pattern = resource.getXPath(context);
            }
        catch (MuseInstantiationException e)
            {
            throw new ValueSourceResolutionError(String.format("Unable to get the XPath from the %s locator configuration", locator_id), e);
            }

        Object param_values = getValue(_params_source, context, false);
        Object[] params;
        if (param_values instanceof List)
            {
            List<Object> list = (List<Object>) param_values;
            params = new String[list.size()];
            for (int i = 0; i < params.length; i++)
                params[i] = list.get(i).toString();
            }
        else
            params = new Object[] { param_values.toString() };

        String xpath = MessageFormat.format(pattern.replaceAll("'","''"), params);
        context.raiseEvent(ValueSourceResolvedEventType.create("formatted XPath", xpath));

        return By.xpath(xpath);
        }

    private final MuseValueSource _params_source;

    public final static String PARAMS_PARAM = "params";
    public final static String TYPE_ID = ParameterizedXPathElementValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromElementExpression(String type, List<ValueSourceConfiguration> arguments, MuseProject project)
            {
            if (type.equals(STRING_EXPRESSION_ID) && arguments.size() == 2)
                {
                ValueSourceConfiguration config = ValueSourceConfiguration.forSource(ParameterizedXPathElementValueSource.TYPE_ID, arguments.get(0));
                config.addSource(ParameterizedXPathElementValueSource.PARAMS_PARAM, arguments.get(1));
                return config;
                }
            return null;
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(TYPE_ID))
                return String.format("<%s:%s:%s>", STRING_EXPRESSION_ID,
                    context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(), context, depth + 1),
                    context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(PARAMS_PARAM), context, depth + 1));
            return null;
            }

        public static final String STRING_EXPRESSION_ID = "px";
        }
    }
