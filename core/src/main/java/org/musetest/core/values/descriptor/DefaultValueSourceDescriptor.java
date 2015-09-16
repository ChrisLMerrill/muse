package org.musetest.core.values.descriptor;

import org.musetest.core.*;

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
        builder.append(getName()).append(" - ").append(getShortDescription()).append("\n");
        builder.append("type id: ").append(getType()).append("\n");
        if (getGroupName() != null)
            builder.append("UI group: ").append(getGroupName()).append("\n");
        if (getLongDescription() != null)
            {
            builder.append("\n");
            builder.append(getLongDescription());
            }
        return builder.toString();
        }

    protected Class _source_class;
    }


