package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("resource")
@MuseValueSourceName("Project Resource")
@MuseValueSourceDescription("#{source}")
public class ProjectResourceValueSource implements MuseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ProjectResourceValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        _id_source = config.getSource().createSource(project);
        if (_id_source == null)
            throw new MuseInstantiationException("Missing required value parameter");

        _project = project;
        }

    @Override
    public MuseResource resolveValue(StepExecutionContext context) throws StepConfigurationError
        {
        Object resolved = _id_source.resolveValue(context);
        if (resolved == null)
            throw new IllegalArgumentException(String.format("Unable to resolve the project resource (id=%s). id value source resolved to null.", _id_source));
        String id = resolved.toString();
        MuseResource resource = _project.findResource(id, MuseResource.class);
        if (resource == null)
            throw new IllegalArgumentException(String.format("Unable to resolve the project resource (id=%s). Search returned null.", id));
        return resource;
        }

    @Override
    public String getDescription()
        {
        return "#" + _id_source;
        }

    private MuseProject _project;
    private final MuseValueSource _id_source;

//    public final static String ID_PARAM = "id";
    public final static String TYPE_ID = ProjectResourceValueSource.class.getAnnotation(MuseTypeId.class).value();
    }