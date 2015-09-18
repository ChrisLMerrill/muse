package org.musetest.core.step;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * Much like CallMacroStep (which it extends), this executes a series of steps defined outside this step
 * (in the called function).
 *
 * This establishes a new variable scope. Sources (other than ID_PARAM) are resolved before pushing the new scope
 * and then the values are set in the new scope.
 *
 * @see Function
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("callfunction")
@MuseStepName("Function")
@MuseStepShortDescription("Call a function")
@MuseInlineEditString("call function {id}")
@MuseStepIcon("glyph:FontAwesome:EXTERNAL_LINK")
@MuseStepTypeGroup("Structure")
@MuseStepLongDescription("The 'id' source is resolved to a string and used to find the function in the project. The steps within the function are then executed as children of the call-function step, but within a new variable scope. Other sources of the call-function step are passed into this new variable scope and will be available to steps within the function. Other local variables that were accessible to the call-function step will not be visible in the new scope, but higher-level scope variables will be (e.g. test parameters).")
@MuseStepDescriptorImplementation(CallFunctionDescriptor.class)
public class CallFunction extends CallMacroStep
    {
    @SuppressWarnings("unused") // called via reflection
    public CallFunction(StepConfiguration config, MuseProject project) throws StepConfigurationError
        {
        super(config, project);
        _config = config;
        }

    @Override
    protected void start(StepExecutionContext context) throws StepConfigurationError
        {
        // resolve the parameters (sources) to be passed to the function BEFORE the new variable scope is created.
        Map<String, ValueSourceConfiguration> sources = _config.getSources();
        Map<String, Object> values = new HashMap<>();
        for (String name : sources.keySet())
            {
            if (name.equals(CallMacroStep.ID_PARAM))
                continue;
            Object value = sources.get(name).createSource(_project).resolveValue(context);
            values.put(name, value);
            }

        super.start(context);

        // store the values in the new variable scope
        for (String name : values.keySet())
            context.getTestExecutionContext().setVariable(name, values.get(name));
        }

    @Override
    protected boolean isCreateNewVariableScope()
        {
        return true;
        }

    private StepConfiguration _config;

    public final static String TYPE_ID = CallFunction.class.getAnnotation(MuseTypeId.class).value();
    }


