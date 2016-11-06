package org.musetest.builtins.value;

import org.musetest.core.*;
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
@MuseSubsourceDescriptor(displayName = "Id", description = "Id of the project resource", type = SubsourceDescriptor.Type.Single)
public class ProjectResourceValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ProjectResourceValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseExecutionError
        {
        super(config, project);
        _id_source = getValueSource(config, true, project);
        _project = project;
        }

    @Override
    public MuseResource resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String id = getValue(_id_source, context, false, String.class);
        MuseResource resource = _project.getResource(_project.findResource(id, MuseResource.class));
        if (resource == null)
            throw new ValueSourceResolutionError(String.format("Unable to resolve the project resource (id=%s). Search returned null.", id));
        return resource;
        }

    private MuseProject _project;
    private final MuseValueSource _id_source;

    public final static String TYPE_ID = ProjectResourceValueSource.class.getAnnotation(MuseTypeId.class).value();
    }