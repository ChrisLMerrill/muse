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
@MuseStringExpressionSupportImplementation(ProjectResourceValueSource.StringExpressionSupport.class)
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
        MuseResource resource = _project.getResourceStorage().getResource(id);
        if (resource == null)
            throw new ValueSourceResolutionError(String.format("Unable to resolve the project resource (id=%s). Search returned null.", id));
        return resource;
        }

    private MuseProject _project;
    private final MuseValueSource _id_source;

    public final static String TYPE_ID = ProjectResourceValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromPrefixedExpression(String prefix, ValueSourceConfiguration expression, MuseProject project)
            {
            if (prefix.equals(OPERATOR))
                {
                ValueSourceConfiguration config = new ValueSourceConfiguration();
                config.setType(ProjectResourceValueSource.TYPE_ID);
                config.setSource(expression);
                return config;
                }
            return null;
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(ProjectResourceValueSource.TYPE_ID))
                {
                if (config.getValue() instanceof String)
                    return OPERATOR + "\"" + config.getValue().toString() + "\"";
                else
                    return OPERATOR + context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(), context, depth + 1);
                }
            return null;
            }

        private final static String OPERATOR = "#";
        }
    }