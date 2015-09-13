package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StringConcatenationDescriptor extends AnnotatedValueSourceDescriptor
    {
    public StringConcatenationDescriptor(MuseProject project)
        {
        super(StringConcatenationSource.TYPE_ID, StringConcatenationSource.class, project);
        }

    @Override
    public String getShortDescription(ValueSourceConfiguration source)
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
            builder.append(_project.getValueSourceDescriptors().get(config).getShortDescription(config));
            first = false;
            }
        return builder.toString();
        }
    }


