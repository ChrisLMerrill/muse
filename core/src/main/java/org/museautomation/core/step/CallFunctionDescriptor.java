package org.museautomation.core.step;

import org.museautomation.core.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.strings.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CallFunctionDescriptor extends AnnotatedStepDescriptor
    {
    public CallFunctionDescriptor(MuseProject project)
        {
        super(CallFunction.class, project);
        }

    @Override
    public String getShortDescription(StepConfiguration step)
        {
        String start = super.getShortDescription(step);
        Map<String, ValueSourceConfiguration> sources = step.getSources();
        if (sources == null || sources.size() == 1)
            return start;

        StringBuilder builder = new StringBuilder();
        ValueSourceConfiguration return_source = step.getSource(CallFunction.RETURN_PARAM);
        if (return_source != null)
            {
            builder.append("$");
            builder.append(getProject().getValueSourceDescriptors().get(return_source).getInstanceDescription(return_source, new StepExpressionContext(getProject(), step)));
            builder.append(" = ");
            }

        builder.append(super.getShortDescription(step));
        if (sources.keySet().size() == 0)
            return builder.toString();

        boolean first = true;
        for (String name : sources.keySet())
            if (!name.equals(CallFunction.ID_PARAM) && !name.equals(CallFunction.RETURN_PARAM))
                {
                if (first)
                    builder.append(" with ");
                else
                    builder.append(", ");
                builder.append(name);
                builder.append("=");
                ValueSourceConfiguration named_source = step.getSource(name);
                builder.append(getProject().getValueSourceDescriptors().get(named_source).getInstanceDescription(named_source, getContext(step)));
                first = false;
                }

        return builder.toString();
        }
    }


