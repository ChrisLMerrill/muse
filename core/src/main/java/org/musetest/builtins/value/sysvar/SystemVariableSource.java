package org.musetest.builtins.value.sysvar;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

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
        context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), value));
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
        public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
            {
            if (config.getType().equals(SystemVariableSource.TYPE_ID))
                if (config.getValue() instanceof String)
                    return OPERATOR + "\"" + config.getValue().toString() + "\"";
                else
                    return OPERATOR + project.getValueSourceStringExpressionSupporters().toString(config.getSource(), depth + 1);
            return null;
            }

        private final static String OPERATOR = "$$";
        }
    }
