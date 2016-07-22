package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.descriptor.*;
import org.slf4j.*;

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
    public CallMacroStep(StepConfiguration config, MuseProject project) throws StepConfigurationError
        {
        super(config, project);
        _config = config;
        _project = project;
        _id = getValueSource(config, ID_PARAM, true, project);
        }

    @Override
    protected StepExecutionContext createStepExecutionContextForChildren(StepExecutionContext context) throws StepExecutionError
        {
        Object id_obj = _id.resolveValue(context);
        if (id_obj == null)
            throw new StepExecutionError("id source resolved to null");
        else if (!(id_obj instanceof String))
            LOG.warn("id source did not resolve to a string. using toString() = " + id_obj.toString());
        ContainsStep resource = _project.findResource(id_obj.toString(), ContainsStep.class);
        if (resource == null)
            throw new StepExecutionError("unable to locate id " + id_obj.toString());

        StepConfiguration step = resource.getStep();
        List<StepConfiguration> steps;
        if (step.getChildren() != null && step.getChildren().size() > 0)
            steps = step.getChildren();
        else
            {
            steps = new ArrayList<>();
            steps.add(step);
            }

        context.raiseEvent(new DynamicStepLoadingEvent(_config, context, steps));
        return new ListOfStepsExecutionContext(context.getTestExecutionContext(), steps, isCreateNewVariableScope(), this);
        }

    @Override
    protected boolean isCreateNewVariableScope()
        {
        return false;
        }

    protected MuseProject _project;
    private StepConfiguration _config;
    private MuseValueSource _id;

    public final static String ID_PARAM = "id";
    public final static String TYPE_ID = CallMacroStep.class.getAnnotation(MuseTypeId.class).value();

    final static Logger LOG = LoggerFactory.getLogger(CallMacroStep.class);
    }


