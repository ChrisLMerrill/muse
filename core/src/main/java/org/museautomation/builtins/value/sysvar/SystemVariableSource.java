package org.museautomation.builtins.value.sysvar;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * Provides access to system variables within the execution context.
 *
 * To provide a system variable, implement SystemVariableProvider interface. Implementations
 * will by dynamically discovered at runtime.
 *
 * @see SystemVariableProvider
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("sysvar")
@MuseValueSourceName("System variable")
@MuseValueSourceShortDescription("get a system-provided variable (by name)")
@MuseValueSourceLongDescription("Evaluates to a system-provided variable. The variable is located by resolving the sub-source to a string and searching for a system variable provider answering to that name.")
@MuseStringExpressionSupportImplementation(SystemVariableSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the system variable to get", type = SubsourceDescriptor.Type.Single)
public class SystemVariableSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public SystemVariableSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _name = getValueSource(config, true, project);
        }

    public MuseValueSource getName()
        {
        return _name;
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String name = getValue(_name, context, false, String.class);
        Object value = context.getProject().getSystemVariableProviders().resolve(name, context);
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), value));
        return value;
        }

    private MuseValueSource _name;

    public final static String TYPE_ID = SystemVariableSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromPrefixedExpression(String prefix, ValueSourceConfiguration expression, MuseProject project)
            {
            if (prefix.equals(OPERATOR))
                {
                ValueSourceConfiguration config = new ValueSourceConfiguration();
                config.setType(SystemVariableSource.TYPE_ID);
                config.setSource(expression);
                return config;
                }
            return null;
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(SystemVariableSource.TYPE_ID))
                if (config.getValue() instanceof String)
                    return OPERATOR + "\"" + config.getValue().toString() + "\"";
                else
                    return OPERATOR + context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(), context, depth + 1);
            return null;
            }

        private final static String OPERATOR = "$$";
        }
    }
