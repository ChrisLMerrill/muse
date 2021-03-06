package org.museautomation.builtins.value;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("add")
@MuseValueSourceName("Add")
@MuseValueSourceShortDescription("Add numeric sub-sources. Concatenate string sub-sources.")
@MuseValueSourceLongDescription("Adds the sources in the sub-source list. If any sub-sources resolve to a numeric value, it will attempt to convert the remainder to numbers, by parsing the string values. If they cannot all be parsed as numbers, they will all be converted to their string form by calling toString() on the object and then concatenated.")
@MuseStringExpressionSupportImplementation(AdditionSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Addends", description = "The sub-sources to add", type = SubsourceDescriptor.Type.List)
public class AdditionSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public AdditionSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _sources = getValueSourceList(config, true, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        List<Object> values = getValues(context, _sources);

        boolean try_numeric = false;
        for (Object value : values)
            if (value instanceof Number)
                try_numeric = true;

        if (try_numeric)
            {
            Long numeric_result = 0L;
            for (Object value : values)
                if (value instanceof Number)
                    numeric_result = numeric_result + ((Number) value).longValue();
                else
                    {
                    try
                        {
                        long parsed = (long) Double.parseDouble(value.toString());
                        numeric_result = numeric_result + parsed;
                        }
                    catch (NumberFormatException e)
                        {
                        numeric_result = null;
                        break;
                        }
                    }

            if (numeric_result != null)
                return numeric_result;
            }

        // else concatentate them
        StringBuilder builder = new StringBuilder();
        for (Object value : values)
            builder.append(value);
        String result = builder.toString();
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), result));
        return result;
        }

    @Override
    public String getDescription()
        {
        return null;
        }

    public MuseValueSource[] getSources()
        {
        return _sources;
        }

    private MuseValueSource[] _sources;

    public final static String TYPE_ID = AdditionSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromBinaryExpression(ValueSourceConfiguration left, String operator, ValueSourceConfiguration right, MuseProject project)
            {
            if (!"+".equals(operator))
                return null;

            List<ValueSourceConfiguration> sources = new ArrayList<>();
            addSources(sources, left);
            addSources(sources, right);

            ValueSourceConfiguration config = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
            config.setSourceList(sources);
            return config;
            }

        private void addSources(List<ValueSourceConfiguration> list, ValueSourceConfiguration source)
            {
            if (AdditionSource.TYPE_ID.equals(source.getType()))
	            list.addAll(source.getSourceList());
            else
                list.add(source);
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(AdditionSource.TYPE_ID))
                {
                StringBuilder builder = new StringBuilder();
                if (depth > 0)
                    builder.append("(");
                boolean first = true;
                for (ValueSourceConfiguration sub_source : config.getSourceList())
                    {
                    if (!first)
                        builder.append(" + ");

                    String stringified = context.getProject().getValueSourceStringExpressionSupporters().toString(sub_source, context, depth + 1);
                    if (stringified == null)
                        return null;
                    builder.append(stringified);
                    first = false;
                    }
                if (depth > 0)
                    builder.append(")");
                return builder.toString();
                }
            return null;
            }
        }
    }