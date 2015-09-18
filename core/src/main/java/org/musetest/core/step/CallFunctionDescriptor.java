package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.*;

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

        StringBuilder builder = new StringBuilder(start);
        builder.append(" with ");

        boolean first = true;
        for (String name : sources.keySet())
            if (!name.equals(CallFunction.ID_PARAM))
                {
                if (!first)
                    builder.append(", ");
                builder.append(name);
                builder.append("=");
                ValueSourceConfiguration named_source = step.getSource(name);
                builder.append(getProject().getValueSourceDescriptors().get(named_source).getInstanceDescription(named_source));
                first = false;
                }

        return builder.toString();
        }
    }


