package org.musetest.core.values.descriptor;

import org.musetest.core.*;
import org.musetest.core.values.*;
import org.musetest.core.values.strings.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")
public class UnknownValueSourceDescriptor implements ValueSourceDescriptor
    {
    UnknownValueSourceDescriptor(String type, MuseProject project)
        {
        _type = type;
        _project = project;
        }

    @Override
    public String getType()
        {
        return _type;
        }

    @Override
    public String getName()
        {
        return _type;
        }

    @Override
    public String getShortDescription()
        {
        return _type;
        }

    @Override
    public String getInstanceDescription(ValueSourceConfiguration source, StringExpressionContext context)
        {
        StringBuilder builder = new StringBuilder(getName());
        builder.append(": ");
        boolean first = true;

        StringExpressionContext source_context = new ValueSourceExpressionContext(context, source);
        if (source.getValue() != null)
            {
            builder.append(source.getValue());
            first = false;
            }
        if (source.getSource() != null)
            {
            appendSourceDescription(builder, source.getSource(), first, source_context);
            first = false;
            }
        if (source.getSourceList() != null && source.getSourceList().size() > 0)
            {
            for (ValueSourceConfiguration list_source : source.getSourceList())
                {
                appendSourceDescription(builder, list_source, first, source_context);
                first = false;
                }
            }
        for (String source_name : source.getSourceNames())
            {
            appendSourceDescription(builder, source.getSource(source_name), first, source_context);
            first = false;
            }
        return builder.toString();
        }

    private void appendSourceDescription(StringBuilder builder, ValueSourceConfiguration source, boolean first, StringExpressionContext context)
        {
        if (!first)
            builder.append(", ");
        builder.append(_project.getValueSourceDescriptors().get(source).getInstanceDescription(source, context));
        }

    @Override
    public String getLongDescription()
        {
        return null;
        }

    @Override
    public String getDocumentationDescription()
        {
        return null;
        }

    @Override
    public String getGroupName()
        {
        return null;
        }

    public MuseProject getProject()
        {
        return _project;
        }

    @Override
    public SubsourceDescriptor[] getSubsourceDescriptors()
        {
        return new SubsourceDescriptor[0];
        }

    String _type;
    protected MuseProject _project;
    }
