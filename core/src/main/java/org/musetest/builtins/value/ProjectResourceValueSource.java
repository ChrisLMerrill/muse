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
@MuseValueSourceShortDescription("Get a project resource by resource id")
@MuseValueSourceLongDescription("Resolves the sub-source and converts it to a string, which is used to lookup the resource in the project. If multiple project resources match the id, the first found will be returned (this behavior may not be consistent from one call to the next).")
@MuseStringExpressionSupportImplementation(ProjectResourceValueSourceStringExpressionSupport.class)
public class ProjectResourceValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ProjectResourceValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
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
            throw new StepConfigurationError(String.format("Unable to resolve the project resource (id=%s). id value source resolved to null.", _id_source));
        String id = resolved.toString();
        MuseResource resource = _project.findResource(id, MuseResource.class);
        if (resource == null)
            throw new StepConfigurationError(String.format("Unable to resolve the project resource (id=%s). Search returned null.", id));
        return resource;
        }

    private MuseProject _project;
    private final MuseValueSource _id_source;

    public final static String TYPE_ID = ProjectResourceValueSource.class.getAnnotation(MuseTypeId.class).value();
    }