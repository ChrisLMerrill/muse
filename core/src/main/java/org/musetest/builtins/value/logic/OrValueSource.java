package org.musetest.builtins.value.logic;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("or")
@MuseValueSourceName("Or")
@MuseValueSourceTypeGroup("Logic")
@MuseValueSourceShortDescription("Logical OR of the sub-sources")
@MuseValueSourceLongDescription("Resolves each operand as a boolean. Returns true if any are true.")
@MuseStringExpressionSupportImplementation(OrValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Operands", description = "subsource to evaluate (expects boolean values)", type = SubsourceDescriptor.Type.List)
public class OrValueSource extends BaseValueSource
    {
    public OrValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _sources = getValueSourceList(config, true, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        List<Object> values = getValues(context, _sources);
        for (Object value : values)
            if (value instanceof Boolean)
                {
                if ((Boolean)value)
                    {
                    context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), Boolean.TRUE));
                    return Boolean.TRUE;
                    }
                }
            else
                {
                if (value == null)
                    throw new ValueSourceResolutionError("Expected the sub-source to resolve to a boolean. Instead, it is null");
                throw new ValueSourceResolutionError(String.format("Expected the sub-source to resolve to a boolean. Instead, got: %s (which is a %s)", value.toString(), value.getClass().getSimpleName()));
                }
        return Boolean.FALSE;
        }

    private MuseValueSource[] _sources;

    public final static String TYPE_ID = OrValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromBooleanExpression(ValueSourceConfiguration left, String operator, ValueSourceConfiguration right, MuseProject project)
            {
            if (!"||".equals(operator))
                return null;

            List<ValueSourceConfiguration> sources = new ArrayList<>();
            addSources(sources, left);
            addSources(sources, right);

            ValueSourceConfiguration config = ValueSourceConfiguration.forType(OrValueSource.TYPE_ID);
            config.setSourceList(sources);
            return config;
            }

        private void addSources(List<ValueSourceConfiguration> list, ValueSourceConfiguration source)
            {
            if (OrValueSource.TYPE_ID.equals(source.getType()))
	            list.addAll(source.getSourceList());
            else
                list.add(source);
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(OrValueSource.TYPE_ID))
                {
                StringBuilder builder = new StringBuilder();
                if (depth > 0)
                    builder.append("(");
                boolean first = true;
                for (ValueSourceConfiguration sub_source : config.getSourceList())
                    {
                    if (!first)
                        builder.append(" || ");

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
