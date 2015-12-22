package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class AdditionDescriptor extends AnnotatedValueSourceDescriptor
    {
    public AdditionDescriptor(MuseProject project)
        {
        super(AdditionSource.TYPE_ID, AdditionSource.class, project);
        }

    @Override
    public String getInstanceDescription(ValueSourceConfiguration source)
        {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        List<ValueSourceConfiguration> list = source.getSourceList();
        if (list == null)
            return null;
        for (ValueSourceConfiguration config : list)
            {
            if (!first)
                builder.append("+");
            builder.append(_project.getValueSourceDescriptors().get(config).getInstanceDescription(config));
            first = false;
            }
        return builder.toString();
        }
    }


