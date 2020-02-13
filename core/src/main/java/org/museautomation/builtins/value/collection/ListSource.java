package org.museautomation.builtins.value.collection;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("list")
@MuseValueSourceName("List")
@MuseValueSourceShortDescription("Creates a list of values.")
@MuseValueSourceLongDescription("Assembles a list from the values in the sub-source list.")
@MuseStringExpressionSupportImplementation(ListSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Elements", description = "The elements to put in the list", type = SubsourceDescriptor.Type.List)
public class ListSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ListSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);

        List<ValueSourceConfiguration> configs = config.getSourceList();
        if (configs == null)
            configs = new ArrayList<>();

        List<MuseValueSource> sources = new ArrayList<>();
        for (ValueSourceConfiguration source : configs)
            sources.add(source.createSource(project));

        _sources = sources.toArray(new MuseValueSource[0]);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        List<Object> values = new ArrayList<>();
        StringBuilder builder = new StringBuilder("[");
        for (MuseValueSource source : _sources)
	        {
	        final Object value = source.resolveValue(context);
	        values.add(value);
	        if (values.size() > 1)
	        	builder.append(",");
	        builder.append(value.toString());
	        }
        builder.append("]");

        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), builder.toString()));
        return values;
        }

    public MuseValueSource[] getSources()
        {
        return _sources;
        }

    private MuseValueSource[] _sources;

    public final static String TYPE_ID = ListSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromArrayExpression(List<ValueSourceConfiguration> elements, MuseProject project)
	        {
            ValueSourceConfiguration config = ValueSourceConfiguration.forType(ListSource.TYPE_ID);
            config.setSourceList(elements);
            return config;
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(ListSource.TYPE_ID))
                {
                StringBuilder builder = new StringBuilder("[");
                boolean first = true;
                for (ValueSourceConfiguration sub_source : config.getSourceList())
                    {
                    if (!first)
                        builder.append(",");

                    String stringified = context.getProject().getValueSourceStringExpressionSupporters().toString(sub_source, context, depth + 1);
                    if (stringified == null)
                        return null;
                    builder.append(stringified);
                    first = false;
                    }
                builder.append("]");
                return builder.toString();
                }
            return null;
            }
        }
    }