package org.museautomation.builtins.value;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("variable")
@MuseValueSourceName("Variable")
@MuseValueSourceShortDescription("get the value assigned to a variable (by variable name)")
@MuseValueSourceLongDescription("Evaluates to the value assigned to a variable in the step execution context. The variable is located by resolving the sub-source to a string and using that as the name of the variable to return. If not found in the local context, it will attempt to find the variable (by name) in higher-level contexts (e.g. the test context).")
@MuseStringExpressionSupportImplementation(VariableValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the variable to get", type = SubsourceDescriptor.Type.Single)
public class VariableValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public VariableValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        if (config.getValue() != null)
            {
            Object value = config.getValue();
            if (!(value instanceof String))
                LOG.warn(String.format("Non-string value provided for VariableValueSource (will convert). Object of type %s was provided, string conversion = %s", value.getClass().getSimpleName(), value.toString()));
            _name = ValueSourceConfiguration.forValue(value).createSource(project);
            }
        else if (config.getSource() != null)
            _name = config.getSource().createSource(project);
        else
            throw new MuseInstantiationException("Missing required parameter (value or valueSource)");
        }

    public MuseValueSource getName()
        {
        return _name;
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Object value = context.getVariable(_name.resolveValue(context).toString());
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), value));
        return value;
        }

    private MuseValueSource _name;

    private final static Logger LOG = LoggerFactory.getLogger(VariableValueSource.class);

    public final static String TYPE_ID = VariableValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromPrefixedExpression(String prefix, ValueSourceConfiguration expression, MuseProject project)
            {
            if (prefix.equals(OPERATOR))
                {
                ValueSourceConfiguration config = new ValueSourceConfiguration();
                config.setType(VariableValueSource.TYPE_ID);
                config.setSource(expression);
                return config;
                }
            return null;
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(VariableValueSource.TYPE_ID))
                if (config.getValue() instanceof String)
                    return OPERATOR + "\"" + config.getValue().toString() + "\"";
                else
                    return OPERATOR + context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(), context,depth + 1);
            return null;
            }

        private final static String OPERATOR = "$";
        }
    }
