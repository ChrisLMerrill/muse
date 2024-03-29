package org.museautomation.core.commandline;

import io.airlift.airline.*;
import org.museautomation.core.*;
import org.museautomation.core.resource.types.*;
import org.slf4j.*;

/**
 * Extension to the Muse command-line launcher to create a project resource.
 *
 * This class is registered as a service in /META-INF/services/org.museautomation.core.commandline.MuseCommand.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Command(name = "create", description = "Create a project resource (test, suite, page, ...)")
public class CreateCommand extends MuseCommand
    {
    @SuppressWarnings("WeakerAccess")
    @Arguments(description = "filename/ID of resource to create", required = true)
    public String resource_id;

    @Option(name = "-t", description = "resource type (defaults to test)")
    public String type;

    @Override
    public void run()
        {
        MuseProject project = openProject();

        ResourceType resource_type;
        if (type == null)
            resource_type = new MuseTask.TaskResourceType();
        else
            resource_type = project.getResourceTypes().forIdIgnoreCase(type);

        if (resource_type == null)
            {
            LOG.error(String.format("Resource type %s not found in the project", type));
            return;
            }

        try
            {
            MuseResource resource = resource_type.create();
            resource.setId(resource_id);
//            ResourceMetadata meta = resource.getMetadata();
//            meta.setSaver(new FromJsonFileResourceFactory()); // defaults to JSON
            String error = project.getResourceStorage().saveResource(resource);
            if (error != null)
                LOG.error(error);
            }
        catch (Exception e)
            {
            LOG.error("An error occurred while creating the resource", e);
            }
        }

    private final static Logger LOG = LoggerFactory.getLogger(CreateCommand.class);
    }


