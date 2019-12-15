package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.events.matching.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("else")
@MuseStepName("Else")
@MuseInlineEditString("else")
@MuseStepIcon("glyph:FontAwesome:QUESTION_CIRCLE")
@MuseStepTypeGroup("Conditionals & Looping")
@MuseStepShortDescription("If the previous if was false, run these steps.")
@MuseStepLongDescription("The result of the previous step is evaluated. If it was not an IF step, this step will fail. If the if step condition was false, then the child steps will be executed.")
public class ElseStep extends BasicCompoundStep
    {
    public ElseStep(StepConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws MuseExecutionError
        {
        if (super.shouldEnter(context))  // use the BasicCompoundStep's logic to only run once
            return !isPreviousIfEntered(context);
        return false;
        }

    static boolean isPreviousIfEntered(StepExecutionContext context) throws MuseExecutionError
        {
        EventLog log = context.getEventLog();
        MuseEvent end_event = log.findLastEvent(new EventTypeMatcher(EndStepEventType.TYPE_ID));
        StepConfiguration if_step = context.getParent().getStepLocator().findStep(EndStepEventType.getStepId(end_event));
        if (!(IfStep.TYPE_ID.equals(if_step.getType()) || ElseIfStep.TYPE_ID.equals(if_step.getType())))
            throw new MuseExecutionError("The previous step is not an If or Else-If step.");
        Object attribute = end_event.getAttribute(IfStep.IF_STEP_ENTERED);
        if (attribute == null)
            throw new MuseExecutionError(String.format("The last %s event does not have a %s attribute.", EndStepEventType.TYPE_ID, IfStep.IF_STEP_ENTERED));
        if (attribute instanceof Boolean)
            return (Boolean) attribute;
        throw new MuseExecutionError(String.format("The %s attribute of the last %s event is a %s. A boolean is required.", IfStep.IF_STEP_ENTERED, EndStepEventType.TYPE_ID, attribute.getClass().getSimpleName()));
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!super.equals(obj))
            return false;
        return obj instanceof ElseStep;
        }

    public final static String TYPE_ID = ElseStep.class.getAnnotation(MuseTypeId.class).value();
    }


