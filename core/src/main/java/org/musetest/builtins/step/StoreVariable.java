package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("store-variable")
@MuseStepName("Set variable")
@MuseStepShortDescription("Set variable to a value")
@MuseInlineEditString("set ${name} = {value}")
@MuseStepIcon("glyph:FontAwesome:USD")
@MuseStepTypeGroup("Variables")
public class StoreVariable extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public StoreVariable(StepConfiguration config, MuseProject project) throws StepExecutionError
        {
        super(config);
        _name = getValueSource(config, NAME_PARAM, true, project);
        _value = getValueSource(config, VALUE_PARAM, true, project);
        }

    @Override
    public StepExecutionResult execute(StepExecutionContext context) throws StepConfigurationError
        {
        String name = getValue(_name, context, false, String.class);
        Object value = getValue(_value, context, true, Object.class);

        context.getTestExecutionContext().setVariable(name, value);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, String.format("%s = %s", name, value));
        }

    private MuseValueSource _name;
    private MuseValueSource _value;

    public final static String NAME_PARAM = "name";
    public final static String VALUE_PARAM = "value";
    public final static String TYPE_ID = StoreVariable.class.getAnnotation(MuseTypeId.class).value();
    }


