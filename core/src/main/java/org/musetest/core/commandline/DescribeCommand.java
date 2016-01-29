package org.musetest.core.commandline;

import io.airlift.airline.*;
import org.musetest.core.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.util.*;

/**
 * Extension to the Muse command-line launcher to describe type of step or value source
 *
 * This class is registered as a service in /META-INF/services/org.musetest.core.commandline.MuseCommand.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Command(name = "describe", description = "Describe a type of step or source")
public class DescribeCommand extends MuseCommand
    {
    @Arguments(description = "The id of the thing to describe (type id of step or source)", required = true)
    public String type_id;

    @Override
    public void run()
        {
        MuseProject project = openProject();

        StepDescriptor descriptor = project.getStepDescriptors().get(type_id);
        if (descriptor == null)
            {
            System.out.println(String.format("type %s is not recognized in the project", type_id));
            return;
            }

        System.out.println(descriptor.getDocumentationDescription());
        }
    }


