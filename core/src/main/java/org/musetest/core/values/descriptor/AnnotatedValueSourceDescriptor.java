package org.musetest.core.values.descriptor;

import org.musetest.core.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
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
        MuseValueSourceInstanceDescription description_annotation = (MuseValueSourceInstanceDescription) _source_class.getAnnotation(MuseValueSourceInstanceDescription.class);
        if (description_annotation != null)
            {
            String description = description_annotation.value();
            DynamicMapFormat format = new DynamicMapFormat(key ->
                {
                if (key.equals(VALUE_KEY))
                    return config.getValue();
                else if (key.equals(SOURCE_KEY))
                    {
                    ValueSourceConfiguration source = config.getSource();
                    if (source != null)
                        return _project.getValueSourceDescriptors().get(source).getInstanceDescription(source);
                    }
                else if (key.startsWith(NAMED_SOURCE_KEY_START) && key.endsWith(NAMED_SOURCE_KEY_END))
                    {
                    String name = key.substring(NAMED_SOURCE_KEY_START.length(), key.length() - NAMED_SOURCE_KEY_END.length());
                    ValueSourceConfiguration source = config.getSourceMap().get(name);
                    if (source != null)
                        return _project.getValueSourceDescriptors().get(source).getInstanceDescription(source);
                    }
                else if (key.startsWith(INDEXED_SOURCE_KEY_START) && key.endsWith(INDEXED_SOURCE_KEY_END))
                    {
                    String index_str = key.substring(INDEXED_SOURCE_KEY_START.length(), key.length() - INDEXED_SOURCE_KEY_END.length());
                    if (index_str.toLowerCase().startsWith("n"))
                        {
                        StringBuilder builder = new StringBuilder();
                        String separator = index_str.substring(1);
                        boolean first = true;
                        for (ValueSourceConfiguration source : config.getSourceList())
                            {
                            if (!first)
                                builder.append(separator);
                            builder.append(_project.getValueSourceDescriptors().get(source).getInstanceDescription(source));
                            first = false;
                            }
                        return builder.toString();
                        }
                    try
                        {
                        int index = Integer.parseInt(index_str);
                        ValueSourceConfiguration source = config.getSourceList().get(index);
                        if (source != null)
                            return _project.getValueSourceDescriptors().get(source).getInstanceDescription(source);
                        }
                    catch (Exception e)
                        {
                        return "? " + key + " not exist ?";
                        }
                    }
                return "?" + key + "?";
                });
            return format.format(description);
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
    }


