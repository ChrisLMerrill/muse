package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.*;

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
            if (name_source != null)
                builder.append(_project.getValueSourceDescriptors().get(name_source).getInstanceDescription(name_source));
            ValueSourceConfiguration amount_source = step.getSources().get(IncrementVariable.AMOUNT_PARAM);
            if (amount_source != null)
                {
                builder.append(" by ");
                builder.append(_project.getValueSourceDescriptors().get(amount_source).getInstanceDescription(amount_source));
                }
            }
        return builder.toString();
        }

    @Override
    public String getInlineEditString()
        {
        return "increment ${name} by {amount}";
        }
    }


