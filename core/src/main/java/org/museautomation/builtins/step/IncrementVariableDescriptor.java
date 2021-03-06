package org.museautomation.builtins.step;

import org.museautomation.core.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.strings.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IncrementVariableDescriptor extends AnnotatedStepDescriptor
    {
    public IncrementVariableDescriptor(MuseProject project)
        {
        super(IncrementVariable.class, project);
        }

    @Override
    public String getShortDescription(StepConfiguration step)
        {
        StringBuilder builder = new StringBuilder("increment $");
        if (step.getSources() != null)
            {
            ValueSourceConfiguration name_source = step.getSources().get(IncrementVariable.NAME_PARAM);
            final StepExpressionContext step_context = getContext(step);
            if (name_source != null)
                builder.append(_project.getValueSourceDescriptors().get(name_source).getInstanceDescription(name_source, step_context));
            ValueSourceConfiguration amount_source = step.getSources().get(IncrementVariable.AMOUNT_PARAM);
            if (amount_source != null)
                {
                builder.append(" by ");
                builder.append(_project.getValueSourceDescriptors().get(amount_source).getInstanceDescription(amount_source, step_context));
                }
            }
        return builder.toString();
        }
    }


