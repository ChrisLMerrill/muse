package org.musetest.selenium.pages;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class PagesElementValueSourceStringExpressionSupport extends BaseValueSourceStringExpressionSupport
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



