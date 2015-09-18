package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("compound")
@MuseStepName("Group")
@MuseInlineEditString("group")
@MuseStepIcon("glyph:FontAwesome:BARS")
@MuseStepTypeGroup("Structure")
@MuseStepShortDescription("Group of steps")
@MuseStepLongDescription("Visually group a collection of steps together under a single parent. The grouping has no effect on execution - they are exceuted in same scope as the siblings to their parent. It is intended as an aid to visual maintenance of the tests.")
public class BasicCompoundStep extends BaseStep implements CompoundStep
    {
    @SuppressWarnings("unused") // called via reflection
    public BasicCompoundStep(StepConfiguration config, MuseProject project)
        {
        super(config);
        _child_list = config.getChildren();
        }

    @Override
    public StepExecutionResult execute(StepExecutionContext context) throws StepExecutionError
        {
        if (shouldEnter(context))
            {
            context.setStepVariable(EXECUTED_MARKER_VAR, true);
            start(context);
            return new BasicStepExecutionResult(StepExecutionStatus.INCOMPLETE);
            }
        else
            {
            finish(context);
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            }
        }

    @Override
    public StepConfigProvider getStepProvider(StepExecutionContext context, StepConfiguration config) throws StepConfigurationError
        {
        // run all the steps in order
        return new LinearListStepConfigurationProvider(_child_list);
        }

    /**
     * Override this to implement entry logic. The default implementation runs the children once.
     * Subclasses can call this to ensure they only run once.
     * Alternatively, subclasses may rely on their own implementation (along with the context and configuration)
     * to run zero or multiple times (i.e. if and while implementations).
     *
     * @param context The context of the current execution
     * @return True if this step should be asked for a StepProvider to execute child steps.
     * @throws StepExecutionError if an configuration error or other bug prevents the step from executing
     */
    protected boolean shouldEnter(StepExecutionContext context) throws StepExecutionError
        {
        Boolean entered = (Boolean) context.getStepVariable(EXECUTED_MARKER_VAR);
        if (entered != null && entered)
            return false;
        return true;
        }

    /**
     * Subclasses override this to perform an action before the children are executed.
     *
     * @param context The context of the current execution
     * @throws StepExecutionError if an configuration error or other bug prevents the step from executing
     */
    protected void start(StepExecutionContext context) throws StepExecutionError
        {
        }

    /**
     * Subclasses override this to perform an action after the children are executed.
     *
     * @param context The context of the current execution
     * @throws StepExecutionError if an configuration error or other bug prevents the step from executing
     */
    protected void finish(StepExecutionContext context) throws StepExecutionError
        {
        }

    private List<StepConfiguration> _child_list = null;

    private final static String EXECUTED_MARKER_VAR = "executed";

    public final static String TYPE_ID = BasicCompoundStep.class.getAnnotation(MuseTypeId.class).value();
    }


