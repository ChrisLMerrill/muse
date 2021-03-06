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
@MuseTypeId("store-variable")
@MuseStepName("Set variable")
@MuseInlineEditString("set ${name} = {value}")
@MuseStepIcon("glyph:FontAwesome:USD")
@MuseStepTypeGroup("Variables")
@MuseStepShortDescription("Set variable to a value")
@MuseStepLongDescription("First, the 'value' source is resolved. Then the 'name' source is resolved to a string and used the store the 'value' result as a variable in the local execution context. Note that variables set this way are scoped such that they may only be visible to subsequent steps, but not steps higher or lower in the call chain (execution stack).")
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the variable", type = SubsourceDescriptor.Type.Named, name = StoreVariable.NAME_PARAM)
@MuseSubsourceDescriptor(displayName = "Value", description = "Source to evaluate and store in the variable", type = SubsourceDescriptor.Type.Named, name = StoreVariable.VALUE_PARAM)
public class StoreVariable extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public StoreVariable(StepConfiguration config, MuseProject project) throws MuseInstantiationException
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

        context.setVariable(name, value);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, String.format("%s = %s", name, value));
        }

    private MuseValueSource _name;
    private MuseValueSource _value;

    public final static String NAME_PARAM = "name";
    public final static String VALUE_PARAM = "value";
    public final static String TYPE_ID = StoreVariable.class.getAnnotation(MuseTypeId.class).value();
    }


