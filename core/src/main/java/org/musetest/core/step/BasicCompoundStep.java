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
public class BasicCompoundStep extends BaseStep implements CompoundStep, ListOfStepsCompletionListener
    {
    @SuppressWarnings("unused") // called via reflection
    public BasicCompoundStep(StepConfiguration config, MuseProject project)
        {
        super(config);
        _child_list = config.getChildren();
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError
        {
        _context = context;
        if (shouldEnter(context))
            {
            StepExecutionContext new_context = createStepExecutionContextForChildren(context);
            context.getTestExecutionContext().getExecutionStack().push(new_context);
            beforeChildrenExecuted(new_context);

            return new BasicStepExecutionResult(StepExecutionStatus.INCOMPLETE);
            }
        else
            {
            afterChildrenExecuted(context);
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            }
        }

    protected StepExecutionContext createStepExecutionContextForChildren(StepExecutionContext context) throws StepExecutionError
        {
        return new ListOfStepsExecutionContext(context.getTestExecutionContext(), _child_list, isCreateNewVariableScope(), this);
        }

    protected boolean isCreateNewVariableScope()
        {
        return false;
        }

    @Override
    public void stepsComplete()
        {
        _context.getTestExecutionContext().getExecutionStack().pop();
        _should_enter = false;
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
        return _should_enter;
        }

    /**
     * Subclasses override this to perform an action before the children are executed.
     *
     * @param new_context
     * @param old_context The context of the current execution
     * @throws StepExecutionError if an configuration error or other bug prevents the step from executing
     */
    protected void beforeChildrenExecuted(StepExecutionContext old_context) throws StepExecutionError
        {
        }

    /**
     * Subclasses override this to perform an action after the children are executed.
     *
     * @param context The context of the current execution
     * @throws StepExecutionError if an configuration error or other bug prevents the step from executing
     */
    protected void afterChildrenExecuted(StepExecutionContext context) throws StepExecutionError
        {
        }

    private boolean _should_enter = true;
    private List<StepConfiguration> _child_list = null;
    private StepExecutionContext _context;

    public final static String TYPE_ID = BasicCompoundStep.class.getAnnotation(MuseTypeId.class).value();
    }


