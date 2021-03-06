package org.museautomation.builtins.value.property;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * Resolves a named property of another value/onject.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("property")
@MuseValueSourceName("Property")
@MuseValueSourceShortDescription("get a named property from an object")
@MuseValueSourceLongDescription("Evaluates the 'name' source to a string and then looks for a property matching that name in the 'target' source.")
@MuseStringExpressionSupportImplementation(PropertySource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the property to access", type = SubsourceDescriptor.Type.Named, name = "name")
@MuseSubsourceDescriptor(displayName = "Target", description = "The object to look for the property in", type = SubsourceDescriptor.Type.Named, name = "target")
public class PropertySource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public PropertySource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _name = getValueSource(config, NAME_PARAM, true, project);
        _target = getValueSource(config, TARGET_PARAM, true, project);
        }

    public MuseValueSource getName()
        {
        return _name;
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String name = getValue(_name, context, false, String.class);
        Object target = getValue(_target, context, false);

        List<PropertyResolver> resolvers = getProject().getPropertyResolvers().getPropertyResolvers();
        for (PropertyResolver resolver : resolvers)
            try
                {
                if (resolver.canResolve(target, name))
                    {
                    Object result = resolver.resolve(target, name);
                    context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), result));
                    return result;
                    }
                }
            catch (Exception e)
                {
                throw new ValueSourceResolutionError(e.getMessage());
                }
        return null;
        }

    private MuseValueSource _name ;
    private MuseValueSource _target;

    public final static String NAME_PARAM = "name";
    public final static String TARGET_PARAM = "target";

    public final static String TYPE_ID = PropertySource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromDotExpression(ValueSourceConfiguration left, ValueSourceConfiguration right, MuseProject project)
            {
            ValueSourceConfiguration config = ValueSourceConfiguration.forType(PropertySource.TYPE_ID);
            config.addSource(PropertySource.TARGET_PARAM, left);
            config.addSource(PropertySource.NAME_PARAM, right);
            return config;
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(PropertySource.TYPE_ID))
                {
                String target = context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(PropertySource.TARGET_PARAM), context, depth + 1);
                String name = context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(PropertySource.NAME_PARAM), context, depth + 1);
                String expression = String.format("%s.%s", target, name);
                if (depth == 0)
                    return expression;
                else
                    return "(" + expression + ")";
                }
            return null;
            }
        }
    }
