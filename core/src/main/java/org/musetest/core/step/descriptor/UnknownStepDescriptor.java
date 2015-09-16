package org.musetest.core.step.descriptor;

import org.musetest.core.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UnknownStepDescriptor implements StepDescriptor
    {
    public UnknownStepDescriptor(String type, MuseProject project)
        {
        _type = type;
        _project = project;
        }

    private String _type;

    @Override
    public String getName()
        {
        return _type;
        }

    @Override
    public String getType()
        {
        return _type;
        }

    @Override
    public String getShortDescription()
        {
        return null;
        }

    @Override
    public String getShortDescription(StepConfiguration step)
        {
        StringBuilder builder = new StringBuilder(step.getType());
        if (step.getSources() != null && step.getSources().size() > 0)
            {
            builder.append(": ");
            boolean first = true;
            for (String source_name : step.getSources().keySet())
                {
                if (!first)
                    builder.append(", ");
                ValueSourceConfiguration source = step.getSources().get(source_name);
                builder.append(source_name);
                builder.append("=");
                builder.append(_project.getValueSourceDescriptors().get(source).getInstanceDescription(source));
                first = false;
                }
            }
        return builder.toString();
        }

    @Override
    public String getInlineEditString()
        {
        return null;
        }

    @Override
    public String getIconDescriptor()
        {
        return ICON;
        }

    @Override
    public boolean isCompound()
        {
        return false;
        }

    @Override
    public String getGroupName()
        {
        return null;
        }

    @Override
    public String getDocumentationDescription()
        {
        return null;
        }

    @Override
    public String getLongDescription()
        {
        return null;
        }

    protected MuseProject _project;

    public final static String ICON = "glyph:FontAwesome:SQUARE_ALT";
    }


