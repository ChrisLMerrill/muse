package org.musetest.core.values.descriptor;

import org.musetest.core.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unchecked")
public class AnnotatedValueSourceDescriptor extends DefaultValueSourceDescriptor
    {
    public AnnotatedValueSourceDescriptor(String type, Class<? extends MuseValueSource> source_class, MuseProject project)
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
    public String getInstanceDescription(ValueSourceConfiguration config)
        {
        MuseStringExpressionSupportImplementation supporter_annotation = (MuseStringExpressionSupportImplementation) _source_class.getAnnotation(MuseStringExpressionSupportImplementation.class);
        if (supporter_annotation != null)
            try
                {
                return ((ValueSourceStringExpressionSupport) supporter_annotation.value().newInstance()).toString(config, _project);
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

    public final static String VALUE_KEY = "value";
    public final static String SOURCE_KEY = "source";
    public final static String NAMED_SOURCE_KEY_START = "source(";
    public final static String NAMED_SOURCE_KEY_END = ")";
    public final static String INDEXED_SOURCE_KEY_START = "source[";
    public final static String INDEXED_SOURCE_KEY_END = "]";

    final static Logger LOG = LoggerFactory.getLogger(AnnotatedValueSourceDescriptor.class);
    }


