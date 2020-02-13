package org.museautomation.builtins.value;

import org.museautomation.core.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NameValuePairDescriptor extends AnnotatedValueSourceDescriptor
    {
    public NameValuePairDescriptor(MuseProject project)
        {
        super(NameValuePairSource.TYPE_ID, NameValuePairSource.class, project);
        }

    @Override
    public String getInstanceDescription(ValueSourceConfiguration source, StringExpressionContext context)
        {
        ValueSourceConfiguration name_source = source.getSource(NameValuePairSource.NAME_PARAM);
        ValueSourceConfiguration value_source = source.getSource(NameValuePairSource.VALUE_PARAM);
        ValueSourceDescriptors descriptors = new ValueSourceDescriptors(context.getProject());
        String name_description = null;
        if (name_source != null)
            name_description = descriptors.get(name_source).getInstanceDescription(name_source, context);
        String value_description = null;
        if (value_source != null)
            value_description = descriptors.get(value_source).getInstanceDescription(value_source, context);
        return String.format("(%s,%s)", name_description, value_description);
        }
    }


