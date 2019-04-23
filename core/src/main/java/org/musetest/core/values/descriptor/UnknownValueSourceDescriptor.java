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
        boolean first = true;

        StringExpressionContext source_context = new ValueSourceExpressionContext(context, source);
        if (source.getValue() != null)
            {
            builder.append("(");
            builder.append(source.getValue());
            first = false;
            }
        if (source.getSource() != null)
            {
            if (first)
                builder.append("(");
            appendSourceDescription(builder, source.getSource(), first, source_context);
            first = false;
            }
        boolean first_in_list = true;
        if (source.getSourceList() != null && source.getSourceList().size() > 0)
            {
            for (ValueSourceConfiguration list_source : source.getSourceList())
                {
                if (first)
                    builder.append("(");
                appendSourceDescription(builder, list_source, first_in_list, source_context);
                first = false;
                first_in_list = false;
                }
            }
        boolean first_in_map = true;
        for (String source_name : source.getSourceNames())
            {
            if (first)
                builder.append("(");
            appendSourceDescription(builder, source_name, source.getSource(source_name), first_in_map, source_context);
            first = false;
            first_in_map = false;
            }
        if (!first)
            builder.append(")");
        return builder.toString();
        }

    private void appendSourceDescription(StringBuilder builder, ValueSourceConfiguration source, boolean first, StringExpressionContext context)
        {
        if (!first)
            builder.append(", ");
        builder.append(_project.getValueSourceDescriptors().get(source).getInstanceDescription(source, context));
        }

    private void appendSourceDescription(StringBuilder builder, String name, ValueSourceConfiguration source, boolean first, StringExpressionContext context)
        {
        if (!first)
            builder.append(", ");
        builder.append(name);
        builder.append("=");
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
