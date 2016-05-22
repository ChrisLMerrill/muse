package org.musetest.core.values.descriptor;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UnknownValueSourceDescriptor implements ValueSourceDescriptor
    {
    public UnknownValueSourceDescriptor(String type, MuseProject project)
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
    public String getInstanceDescription(ValueSourceConfiguration source)
        {
        StringBuilder builder = new StringBuilder(getName());
        builder.append(": ");
        boolean first = true;
        if (source.getValue() != null)
            {
            builder.append(source.getValue());
            first = false;
            }
        if (source.getSource() != null)
            {
            appendSourceDescription(builder, source.getSource(), first);
            first = false;
            }
        if (source.getSourceList() != null && source.getSourceList().size() > 0)
            {
            for (ValueSourceConfiguration list_source : source.getSourceList())
                {
                appendSourceDescription(builder, list_source, first);
                first = false;
                }
            }
        if (source.getSourceMap() != null && source.getSourceMap().size() > 0)
            {
            for (String source_name : source.getSourceMap().keySet())
                {
                appendSourceDescription(builder, source.getSourceMap().get(source_name), first);
                first = false;
                }
            }
        return builder.toString();
        }

    private void appendSourceDescription(StringBuilder builder, ValueSourceConfiguration source, boolean first)
        {
        if (!first)
            builder.append(", ");
        builder.append(_project.getValueSourceDescriptors().get(source).getInstanceDescription(source));
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

    protected String _type;
    protected MuseProject _project;
    }
