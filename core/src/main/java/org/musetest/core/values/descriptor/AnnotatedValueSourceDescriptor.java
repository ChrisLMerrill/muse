package org.musetest.core.values.descriptor;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unchecked")
public class AnnotatedValueSourceDescriptor extends DefaultValueSourceDescriptor
    {
    AnnotatedValueSourceDescriptor(String type, Class<? extends MuseValueSource> source_class, MuseProject project)
        {
        super(type, source_class, project);
        }

    @Override
    public String getName()
        {
        MuseValueSourceName name_annotation = (MuseValueSourceName) _source_class.getAnnotation(MuseValueSourceName.class);
        if (name_annotation != null)
            return name_annotation.value();
        return super.getName();
        }

    @Override
    public String getShortDescription()
        {
        MuseValueSourceShortDescription annotation = (MuseValueSourceShortDescription) _source_class.getAnnotation(MuseValueSourceShortDescription.class);
        if (annotation != null)
            return annotation.value();
        return super.getShortDescription();
        }

    @Override
    public String getLongDescription()
        {
        MuseValueSourceLongDescription annotation = (MuseValueSourceLongDescription) _source_class.getAnnotation(MuseValueSourceLongDescription.class);
        if (annotation != null)
            return annotation.value();
        return super.getShortDescription();
        }

    @Override
    public String getInstanceDescription(ValueSourceConfiguration source, StringExpressionContext context)
	    {
	    ValueSourceStringExpressionSupporters supporters = new ValueSourceStringExpressionSupporters(context.getProject());
        String editable_string = supporters.toString(source, context);
        if (editable_string != null)
            return editable_string;

        return super.getShortDescription();
        }

    @Override
    public String getGroupName()
        {
        MuseValueSourceTypeGroup group_name = (MuseValueSourceTypeGroup) _source_class.getAnnotation(MuseValueSourceTypeGroup.class);
        if (group_name != null)
            return group_name.value();

        return super.getGroupName();
        }

    @Override
    public SubsourceDescriptor[] getSubsourceDescriptors()
        {
        SubsourceDescriptor[] descriptors = SubsourceDescriptor.getSubsourceDescriptors(_source_class);
        if (descriptors == null)
            return super.getSubsourceDescriptors();
        return descriptors;
        }
    }


