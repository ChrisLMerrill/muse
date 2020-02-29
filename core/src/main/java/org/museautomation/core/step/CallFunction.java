package org.museautomation.core.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

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
@MuseStepLongDescription("The 'id' source is resolved to a string and used to find the function in the project. The steps within the function are then executed as children of the call-function step, and if a value is returned, it is stored as a variable with the name provided by the 'return' source.  The steps of the function are executed within a new variable scope. Other sources of the call-function step are passed into this new variable scope and will be available to steps within the function. Other local variables that were accessible to the call-function step will not be visible in the new scope, but higher-level scope variables will be (e.g. test parameters).")
@MuseStepDescriptorImplementation(CallFunctionDescriptor.class)
@MuseSubsourceDescriptor(displayName = "Function id", description = "The id of the function to call", type = SubsourceDescriptor.Type.Named, name = CallFunction.ID_PARAM, defaultValue = "function-id")
@MuseSubsourceDescriptor(displayName = "Return variable", description = "The name of the variable to store the return value in", type = SubsourceDescriptor.Type.Named, name = CallFunction.RETURN_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Parameters", description = "Parameters to pass to the function", type = SubsourceDescriptor.Type.Map, optional = true)
public class CallFunction extends CallMacroStep
    {
    @SuppressWarnings("unused") // called via reflection
    public CallFunction(StepConfiguration config, MuseProject project)
        {
        super(config, project);
        _config = config;
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws MuseExecutionError
        {
        boolean enter = super.shouldEnter(context);
        if (!enter)
        	return false;

        Map<String, ValueSourceConfiguration> sources = _config.getSources();
        // resolve the parameters (sources) to be passed to the function BEFORE the new variable scope is created.
        for (String name : sources.keySet())
            {
            if (name.equals(ID_PARAM) || name.equals(RETURN_PARAM))
                continue;
            resolveAndAddParameter(name, sources.get(name), context);
            }

        return true;
        }

    @Override
    protected void beforeChildrenExecuted(StepExecutionContext context) throws MuseExecutionError
        {
        super.beforeChildrenExecuted(context);

        // store the values in the new variable scope
        for (String name : _values_to_pass.keySet())
            context.setVariable(name, _values_to_pass.get(name));
        }

    @Override
    protected boolean isCreateNewVariableScope()
        {
        return true;
        }

    public void returned(StepExecutionContext context, Object return_value) throws MuseExecutionError
        {
        MuseValueSource return_var_name_source = getValueSource(_config, RETURN_PARAM, false, context.getProject());
        if (return_var_name_source != null)
            {
            String return_var_name = getValue(return_var_name_source, context, false, String.class);
            context.setVariable(return_var_name, return_value);
            }

        BasicStepExecutionResult result = new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        context.raiseEvent(EndStepEventType.create(_config, context, result));
        context.stepComplete(this, result);
        }

    @SuppressWarnings("WeakerAccess") // allow subclasses to use this
    protected void resolveAndAddParameter(String name, ValueSourceConfiguration value_source, MuseExecutionContext context) throws MuseExecutionError
	    {
	    Object value = value_source.createSource(_project).resolveValue(context);
	    _values_to_pass.put(name, value);
	    }

    private StepConfiguration _config;
    private Map<String, Object> _values_to_pass = new HashMap<>();

    public final static String RETURN_PARAM = "return";

    public final static String TYPE_ID = CallFunction.class.getAnnotation(MuseTypeId.class).value();
    }