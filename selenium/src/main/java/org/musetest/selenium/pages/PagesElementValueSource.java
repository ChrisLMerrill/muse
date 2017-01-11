package org.musetest.selenium.pages;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;

import java.util.*;

/**
 * Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("page-element")
@MuseValueSourceName("Element by page/element lookup")
@MuseValueSourceShortDescription("Locates a Selenium WebElement from the page/element specified by the subsource")
@MuseValueSourceTypeGroup("Element.Locate")
@MuseStringExpressionSupportImplementation(PagesElementValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Page id", description = "The id of the page (in the project) to lookup the element in", type = SubsourceDescriptor.Type.Named, name = PagesElementValueSource.PAGE_PARAM_ID)
@MuseSubsourceDescriptor(displayName = "Element id", description = "The id of the element to lookup in the page", type = SubsourceDescriptor.Type.Named, name = PagesElementValueSource.ELEMENT_PARAM_ID)
public class PagesElementValueSource extends BaseSeleniumValueSource
    {
    public PagesElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        upgrade(config);
        _page_source = config.getSource(PagesElementValueSource.PAGE_PARAM_ID).createSource(project);
        _element_source = config.getSource(PagesElementValueSource.ELEMENT_PARAM_ID).createSource(project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        MuseProject project = context.getProject();
        Object value = _page_source.resolveValue(context);
        if (!(value instanceof String))
            throw new ValueSourceResolutionError(String.format("The 'page' parameter must resolve to a String. Instead, it was %s (a %s)", value, value.getClass().getSimpleName()));
        String page_id = (String) value;

        value = _element_source.resolveValue(context);
        if (!(value instanceof String))
            throw new ValueSourceResolutionError(String.format("The 'element' parameter must resolve to a String. Instead, it was %s (a %s)", value, value.getClass().getSimpleName()));
        String element_id = (String) value;

        PageElement element = new PageElementLocator(project).find(page_id, element_id);
        if (element == null)
            throw new ValueSourceResolutionError(String.format("Element not found in the project...unable to lookup locator by page.element key '%s.%s'", page_id, element_id));

        ValueSourceConfiguration element_locator_config = element.getLocator();
        if (element_locator_config == null)
            throw new ValueSourceResolutionError(String.format("No locator configured for page-element '%s.%s'", page_id, element_id));

        try
            {
            return element_locator_config.createSource(project).resolveValue(context);
            }
        catch (MuseInstantiationException e)
            {
            throw new ValueSourceResolutionError("Unable to resolve page element source - unable to instantiate the specified locator due to: " + e.getMessage(), e);
            }
        }

    private static void upgrade(ValueSourceConfiguration config)
        {
        ValueSourceConfiguration subsource = config.getSource();
        if (subsource != null
            && subsource.getType().equals(StringValueSource.TYPE_ID)
            && subsource.getValue() != null
            && subsource.getValue().toString().contains("."))
            {
            StringTokenizer tokenizer = new StringTokenizer(subsource.getValue().toString(), ".");
            config.addSource(PagesElementValueSource.PAGE_PARAM_ID, ValueSourceConfiguration.forValue(tokenizer.nextToken()));
            config.addSource(PagesElementValueSource.ELEMENT_PARAM_ID, ValueSourceConfiguration.forValue(tokenizer.nextToken()));
            config.setSource(null);
            }
        }

    private MuseValueSource _page_source;
    private MuseValueSource _element_source;

    public final static String TYPE_ID = PagesElementValueSource.class.getAnnotation(MuseTypeId.class).value();
    public final static String PAGE_PARAM_ID = "page";
    public final static String ELEMENT_PARAM_ID = "element";

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromElementExpression(String type, List<ValueSourceConfiguration> arguments, MuseProject project)
            {
            if (type.equals(STRING_TYPE_ID) && (arguments.size() == 1 || arguments.size() == 2))
                {
                ValueSourceConfiguration page_source;
                ValueSourceConfiguration element_source;
                if (arguments.size() == 1 && arguments.get(0).getType().equals(StringValueSource.TYPE_ID))  // for backwards compatibility
                    {
                    StringTokenizer tokenizer = new StringTokenizer((String) (arguments.get(0).getValue()), ".");
                    page_source = ValueSourceConfiguration.forValue(tokenizer.nextToken());
                    element_source = ValueSourceConfiguration.forValue(tokenizer.nextToken());
                    }
                else if (arguments.size() == 2)  // the preferred method
                    {
                    page_source = arguments.get(0);
                    element_source = arguments.get(1);
                    }
                else
                    return null;
                ValueSourceConfiguration source = ValueSourceConfiguration.forType(PagesElementValueSource.TYPE_ID);
                source.addSource(PagesElementValueSource.PAGE_PARAM_ID, page_source);
                source.addSource(PagesElementValueSource.ELEMENT_PARAM_ID, element_source);
                return source;
                }
            return null;
            }

        @Override
        public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
            {
            if (config.getType().equals(PagesElementValueSource.TYPE_ID))
                {
                PagesElementValueSource.upgrade(config);
                StringBuilder builder = new StringBuilder();
                if (depth > 0)
                    builder.append("(");
                if (config.getSource(PagesElementValueSource.PAGE_PARAM_ID) != null
                    && config.getSource(PagesElementValueSource.PAGE_PARAM_ID).getType().equals(StringValueSource.TYPE_ID)
                    && config.getSource(PagesElementValueSource.PAGE_PARAM_ID) != null
                    && config.getSource(PagesElementValueSource.ELEMENT_PARAM_ID).getType().equals(StringValueSource.TYPE_ID))
                    builder.append(String.format("<%s:\"%s.%s\">", STRING_TYPE_ID, config.getSource(PagesElementValueSource.PAGE_PARAM_ID).getValue(), config.getSource(PagesElementValueSource.ELEMENT_PARAM_ID).getValue()));
                else
                    builder.append(String.format("<%s:%s:%s>", STRING_TYPE_ID, project.getValueSourceStringExpressionSupporters().toString(config.getSource(PagesElementValueSource.PAGE_PARAM_ID), depth + 1), project.getValueSourceStringExpressionSupporters().toString(config.getSource(PagesElementValueSource.ELEMENT_PARAM_ID), depth + 1)));
                if (depth > 0)
                    builder.append(")");
                return builder.toString();
                }
            return null;
            }

        public final static String STRING_TYPE_ID = "page";
        }
    }


