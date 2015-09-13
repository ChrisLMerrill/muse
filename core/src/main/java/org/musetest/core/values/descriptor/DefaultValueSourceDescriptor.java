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

    protected Class _source_class;
    }


