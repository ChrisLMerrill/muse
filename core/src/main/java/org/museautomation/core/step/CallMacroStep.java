package org.museautomation.core.step;

import org.jetbrains.annotations.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * Executes the steps contained within a Macro.
 *
 * Note that this does NOT execute those steps within a separate variable scope, despite this class extending
 * ScopedGroup. It overrides #isCreateNewVariableScope to disable that behavior. That seems a bit strange, but
 * CallFunction builds on the basic function of CallMacroStep and it needs to be scoped.  We need multiple-inheritance
 * to do this cleanly (yuck), but this will have to suffice.
 *
 * @see Macro
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("callmacro")
@MuseStepName("Macro")
@MuseInlineEditString("call macro {id}")
@MuseStepIcon("glyph:FontAwesome:EXTERNAL_LINK")
@MuseStepTypeGroup("Structure")
@MuseStepLongDescription("The 'id' source is resolved to a string and used to find the macro in the project. The steps within the macro are then executed as children of the call-macro step, within the same variable scope as the parent. This means that steps within the macro have access to the same variables as the caller.")
@MuseSubsourceDescriptor(displayName = "Macro name", description = "The name (resource id) of the macro to call", type = SubsourceDescriptor.Type.Named, name = CallMacroStep.ID_PARAM)
public class CallMacroStep extends ScopedGroup
    {
    @SuppressWarnings("unused") // called via reflection
    public CallMacroStep(StepConfiguration config, MuseProject project)
        {
        super(config, project);
        _config = config;
        _project = project;
        }

    @Override
    protected StepExecutionContext createStepExecutionContextForChildren(StepExecutionContext context) throws MuseExecutionError
        {
        String id = getStepsId(context);
        ContainsStep resource = _project.getResourceStorage().getResource(id, ContainsStep.class);
   	    if (resource == null)
   	        throw new StepExecutionError("unable to locate project resource, id=" + id);

        StepConfiguration step = resource.getStep();
        List<StepConfiguration> steps;
        if (step.getChildren() != null && step.getChildren().size() > 0)
            steps = step.getChildren();
        else
            {
            steps = new ArrayList<>();
            steps.add(step);
            }

        context.getStepLocator().loadSteps(steps);
        context.raiseEvent(DynamicStepLoadingEventType.create(_config, steps));
        return new ListOfStepsExecutionContext(context.getParent(), steps, isCreateNewVariableScope(), this);
        }

    /**
     * Get the id of the project resource that contains the steps that should be run.
     */
    @NotNull
    @SuppressWarnings("WeakerAccess")
    protected String getStepsId(StepExecutionContext context) throws MuseExecutionError
	    {
	    MuseValueSource id_source = getValueSource(_config, ID_PARAM, true, context.getProject());
	    return BaseValueSource.getValue(id_source, context, false, String.class);
	    }

    @Override
    protected boolean isCreateNewVariableScope()
        {
        return false;
        }

    protected MuseProject _project;
    private StepConfiguration _config;

    public final static String ID_PARAM = "id";
    public final static String TYPE_ID = CallMacroStep.class.getAnnotation(MuseTypeId.class).value();

    }