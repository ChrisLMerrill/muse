package org.museautomation.builtins.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("store-output")
@MuseStepName("Store output")
@MuseInlineEditString("store {value} as ${name}")
@MuseStepIcon("glyph:FontAwesome:USD")
@MuseStepTypeGroup("Variables")
@MuseStepShortDescription("Store a value as a task output")
@MuseStepLongDescription("First, the 'value' source is resolved. Then the 'name' source is resolved to a string and used the store the 'value' result as a task output.")
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the output", type = SubsourceDescriptor.Type.Named, name = StoreOutput.NAME_PARAM)
@MuseSubsourceDescriptor(displayName = "Value", description = "Source to evaluate and store as an output", type = SubsourceDescriptor.Type.Named, name = StoreOutput.VALUE_PARAM)
public class StoreOutput extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public StoreOutput(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _name = getValueSource(config, NAME_PARAM, true, project);
        _value = getValueSource(config, VALUE_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        String name = getValue(_name, context, false, String.class);
        Object value = getValue(_value, context, true, Object.class);

        context.outputs().storeOutput(name, value);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, String.format("Store %s as output named %s", value, name));
        }

    private MuseValueSource _name;
    private MuseValueSource _value;

    public final static String NAME_PARAM = "name";
    public final static String VALUE_PARAM = "value";
    public final static String TYPE_ID = StoreOutput.class.getAnnotation(MuseTypeId.class).value();
    }