package org.museautomation.core.values.descriptor;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultValueSourceDescriptor extends UnknownValueSourceDescriptor
    {
    public DefaultValueSourceDescriptor(String type, Class<? extends MuseValueSource> source_class, MuseProject project)
        {
        super(type, project);
        _source_class = source_class;
        }

    @Override
    public String getName()
        {
        return _source_class.getSimpleName();
        }

    @Override
    public String getShortDescription()
        {
        return getName() + " (" + _type + ")";
        }

    @Override
    public String getDocumentationDescription()
        {
        StringBuilder builder = new StringBuilder();
        builder.append(getName()).append(" (").append(getType()).append(") - ").append(getShortDescription()).append("\n");
        if (getGroupName() != null)
            builder.append("UI group: ").append(getGroupName()).append("\n");
        if (getLongDescription() != null)
            {
            builder.append(getLongDescription());
            builder.append("\n");
            }
        SubsourceDescriptor[] parameters = getSubsourceDescriptors();
        if (parameters.length > 0)
            {
            builder.append("Sub-sources:\n");
            for (SubsourceDescriptor param : parameters)
                {
                builder.append("  ");
                builder.append(param.getDisplayName());
                builder.append(" (");
                builder.append(param.getResolutionType().getSimpleName());
                if (param.isOptional())
                    builder.append(", optional");
                builder.append(") - ");
                builder.append(param.getDescription());
                builder.append("\n");
                }
            }
        return builder.toString();
        }

    protected Class _source_class;
    }


