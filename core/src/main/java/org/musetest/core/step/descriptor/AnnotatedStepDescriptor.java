package org.musetest.core.step.descriptor;

import org.musetest.core.*;
import org.musetest.core.step.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class AnnotatedStepDescriptor extends DefaultStepDescriptor
    {
    public AnnotatedStepDescriptor(Class step_class, MuseProject project)
        {
        super(step_class, project);
        _step_class = step_class;
        }

    @Override
    public String getType()
        {
        MuseTypeId type_id = (MuseTypeId) _step_class.getAnnotation(MuseTypeId.class);
        if (type_id != null)
            return type_id.value();

        return super.getType();
        }

    @Override
    public String getName()
        {
        MuseStepName name = (MuseStepName) _step_class.getAnnotation(MuseStepName.class);
        if (name != null)
            return name.value();

        return super.getName();
        }

    @Override
    public String getShortDescription()
        {
        MuseStepShortDescription description = (MuseStepShortDescription) _step_class.getAnnotation(MuseStepShortDescription.class);
        if (description != null)
            return description.value();
        return super.getShortDescription();
        }

    @Override
    public String getLongDescription()
        {
        MuseStepLongDescription description = (MuseStepLongDescription) _step_class.getAnnotation(MuseStepLongDescription.class);
        if (description != null)
            return description.value();
        return super.getShortDescription();
        }

    @Override
    public String getShortDescription(StepConfiguration step)
        {
        String custom_description = (String) step.getMetadataField(StepConfiguration.META_DESCRIPTION);
        if (custom_description != null)
            return custom_description;

        String edit_string = null;
        MuseInlineEditString description_annotation = (MuseInlineEditString) _step_class.getAnnotation(MuseInlineEditString.class);
        if (description_annotation != null && description_annotation.value() != null)
            edit_string = description_annotation.value();

        if (edit_string != null)
            {
            DynamicMapFormat format = new DynamicMapFormat(key ->
                {
                if (key.startsWith(NAMED_SOURCE_KEY_START) && key.endsWith(NAMED_SOURCE_KEY_END))
                    {
                    String name = key.substring(NAMED_SOURCE_KEY_START.length(), key.length() - 1);
                    ValueSourceConfiguration source = step.getSources().get(name);
                    if (source != null)
                        return _project.getValueSourceDescriptors().get(source).getInstanceDescription(source);
                    }
                else if (step.getSource(key) != null)
                    {
                    ValueSourceConfiguration source = step.getSources().get(key);
                    return _project.getValueSourceDescriptors().get(source).getInstanceDescription(source);
                    }
                return "?" + key + "?";
                });
            return format.format(edit_string);
            }

        return super.getShortDescription(step);
        }

    @Override
    public String getInlineEditString()
        {
        MuseInlineEditString description_annotation = (MuseInlineEditString) _step_class.getAnnotation(MuseInlineEditString.class);
        if (description_annotation != null)
            return description_annotation.value();
        return null;
        }

    @Override
    public String getIconDescriptor()
        {
        MuseStepIcon icon_annotation = (MuseStepIcon) _step_class.getAnnotation(MuseStepIcon.class);
        if (icon_annotation != null)
            return icon_annotation.value();
        return super.getIconDescriptor();
        }

    @Override
    public String getGroupName()
        {
        MuseStepTypeGroup group_name = (MuseStepTypeGroup) _step_class.getAnnotation(MuseStepTypeGroup.class);
        if (group_name != null)
            return group_name.value();

        return super.getGroupName();
        }

    final private Class _step_class;

    public final static String NAMED_SOURCE_KEY_START = "source(";
    public final static String NAMED_SOURCE_KEY_END = ")";
    }


