package org.musetest.core.values.descriptor;

import org.musetest.core.*;
import org.musetest.core.values.*;
import org.musetest.core.values.strings.*;
import org.slf4j.*;

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
        MuseStringExpressionSupportImplementation supporter_annotation = (MuseStringExpressionSupportImplementation) _source_class.getAnnotation(MuseStringExpressionSupportImplementation.class);
        if (supporter_annotation != null)
            try
                {
                return ((ValueSourceStringExpressionSupport) supporter_annotation.value().newInstance()).toString(source, context);
                }
            catch (InstantiationException | IllegalAccessException e)
                {
                LOG.error("Unable to use expression supporter for generating instance description", e);
                // ok, use the alternative
                }

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

    private final static Logger LOG = LoggerFactory.getLogger(AnnotatedValueSourceDescriptor.class);
    }


