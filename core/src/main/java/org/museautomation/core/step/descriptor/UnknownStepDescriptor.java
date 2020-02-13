package org.museautomation.core.step.descriptor;

import org.museautomation.core.*;
import org.museautomation.core.step.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.core.values.strings.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UnknownStepDescriptor implements StepDescriptor
    {
    UnknownStepDescriptor(String type, MuseProject project)
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
            boolean first = true;
            for (String source_name : step.getSources().keySet())
                {
                if (first)
                    builder.append("(");
                if (!first)
                    builder.append(",");
                ValueSourceConfiguration source = step.getSources().get(source_name);
                builder.append(source_name);
                builder.append("=");
                builder.append(_project.getValueSourceDescriptors().get(source).getInstanceDescription(source, new StepExpressionContext(getProject(), step)));
                first = false;
                }
            if (!first)
                builder.append(")");
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
    public ColorDescriptor getIconColor()
        {
        return RgbColorDescriptor.RED;
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

    @Override
    public SubsourceDescriptor[] getSubsourceDescriptors()
        {
        return new SubsourceDescriptor[0];
        }

    public MuseProject getProject()
        {
        return _project;
        }

    protected MuseProject _project;

    public final static String ICON = "glyph:FontAwesome:EXCLAMATION_TRIANGLE";
    }


