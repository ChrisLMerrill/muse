package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
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
    public StepExecutionResult execute(StepExecutionContext context) throws StepExecutionError
        {
        if (context.getStepVariable(STEPS_VAR) != null)
            {
            finish(context);
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            }

        start(context);

        Object id_obj = _id.resolveValue(context);
        if (id_obj == null)
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "id source resolved to null");
        else if (!(id_obj instanceof String))
            LOG.warn("id source did not resolve to a string. using toString() = " + id_obj.toString());
        ContainsStep resource = _project.findResource(id_obj.toString(), ContainsStep.class);
        if (resource == null)
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "unable to locate id " + id_obj.toString());

        StepConfiguration step = resource.getStep();
        List<StepConfiguration> steps;
        if (step.getChildren() != null && step.getChildren().size() > 0)
            steps = step.getChildren();
        else
            {
            steps = new ArrayList<>();
            steps.add(step);
            }

        context.setStepVariable(STEPS_VAR, steps);
        context.getTestExecutionContext().raiseEvent(new DynamicStepLoadingEvent(_config, context, steps));
        return new BasicStepExecutionResult(StepExecutionStatus.INCOMPLETE);
        }

    @Override
    @SuppressWarnings("unchecked")
    public StepConfigProvider getStepProvider(StepExecutionContext context, StepConfiguration config) throws StepConfigurationError
        {
        List<StepConfiguration> steps = (List<StepConfiguration>) context.getStepVariable(STEPS_VAR);
        return new LinearListStepConfigurationProvider(steps);
        }

    @Override
    protected boolean isCreateNewVariableScope()
        {
        return false;
        }

    protected MuseProject _project;
    private StepConfiguration _config;
    private MuseValueSource _id;
    private static String STEPS_VAR = "STEPS";

    public final static String ID_PARAM = "id";
    public final static String TYPE_ID = CallMacroStep.class.getAnnotation(MuseTypeId.class).value();

    final static Logger LOG = LoggerFactory.getLogger(CallMacroStep.class);
    }


