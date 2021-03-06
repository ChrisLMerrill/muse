package org.museautomation.core.step.descriptor;

import org.museautomation.core.*;
import org.museautomation.core.step.*;
import org.museautomation.core.values.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultStepDescriptor extends UnknownStepDescriptor
    {
    protected DefaultStepDescriptor(Class<? extends MuseStep> step_class, MuseProject project)
        {
        super("?" + step_class.getSimpleName() + "?", project);
        _step_class = step_class;

        MuseTypeId type_annotation = step_class.getAnnotation(MuseTypeId.class);
        if (type_annotation != null)
            _type = type_annotation.value();
        else
            {
            _type = step_class.getSimpleName();
            LOG.info(String.format("No type declared for class %s, using class name.", step_class.getSimpleName()));
            }
        }

    @Override
    public String getType()
        {
        if (_type != null)
            return _type;
        if (_step_class != null)
            return _step_class.getSimpleName();
        LOG.error("No type information available. This should not be possible if the constructors vetted their parameters correctly.");
        return "?type?";
        }

    @Override
    public String getName()
        {
        if (_step_class != null)
            return _step_class.getSimpleName();
        return _type;
        }

    @Override
    public String getShortDescription()
        {
        return null;
        }

    @Override
    public String getShortDescription(StepConfiguration step)
        {
        String custom_description = (String) step.getMetadataField(StepConfiguration.META_DESCRIPTION);
        if (custom_description != null)
            return custom_description;

        return super.getShortDescription(step);
        }

    @Override
    public ColorDescriptor getIconColor()
        {
        return RgbColorDescriptor.BLACK;
        }

    @Override
    public String getInlineEditString()
        {
        return null;
        }

    @Override
    public boolean isCompound()
        {
        return CompoundStep.class.isAssignableFrom(_step_class);
        }

    @Override
    public String getDocumentationDescription()
        {
        StringBuilder builder = new StringBuilder();
        builder.append(getName()).append(" - ").append(getShortDescription()).append("\n");
        builder.append("type id: ").append(getType()).append("\n");
        if (getGroupName() != null)
            builder.append("UI group: ").append(getGroupName()).append("\n");

        SubsourceDescriptor[] subsources = getSubsourceDescriptors();
        if (subsources != null && subsources.length > 0)
            {
            builder.append("\nParameters:\n");
            for (SubsourceDescriptor subsource : subsources)
                            builder.append(subsource.getOneLineSummary()).append("\n");
            }

        if (getLongDescription() != null)
            {
            builder.append("\n");
            builder.append(getLongDescription());
            }
        return builder.toString();
        }

    private String _type;
    private Class<? extends MuseStep> _step_class;

    private final static Logger LOG = LoggerFactory.getLogger(DefaultStepDescriptor.class);
    }