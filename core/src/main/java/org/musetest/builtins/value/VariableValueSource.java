package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("variable")
@MuseValueSourceName("Variable")
@MuseValueSourceInstanceDescription("${source}")
@MuseValueSourceShortDescription("get the value assigned to a variable (by variable name)")
@MuseValueSourceLongDescription("Returns the value assigned to a variable in the step execution context. The variable is located by resolving the sub-source to a string and using that as the name of the variable to return. If not found in the local context, it will attempt to find the variable (by name) in higher-level contexts (e.g. the test context).")
public class VariableValueSource implements MuseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public VariableValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        _configuration = config;
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
    public Object resolveValue(StepExecutionContext context) throws StepConfigurationError
        {
        Object value = context.getTestExecutionContext().getVariable(_name.resolveValue(context).toString());
        context.getTestExecutionContext().raiseEvent(new ValueSourceResolvedEvent(context.getTestExecutionContext().getProject().getValueSourceDescriptors().get(_configuration).getInstanceDescription(_configuration), value));
        return value;
        }

    @Override
    public String getDescription()
        {
        return "$" + _name;
        }

    private MuseValueSource _name;
    private ValueSourceConfiguration _configuration;

    final static Logger LOG = LoggerFactory.getLogger(VariableValueSource.class);

    public final static String TYPE_ID = VariableValueSource.class.getAnnotation(MuseTypeId.class).value();
    }
