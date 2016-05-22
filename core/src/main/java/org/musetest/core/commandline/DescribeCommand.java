package org.musetest.core.commandline;

import io.airlift.airline.*;
import org.musetest.core.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.util.*;
import org.musetest.core.values.descriptor.*;

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

        StepDescriptor step = project.getStepDescriptors().get(type_id, true);
        if (step != null)
            {
            System.out.println(step.getDocumentationDescription());
            return;
            }

        ValueSourceDescriptor source = project.getValueSourceDescriptors().get(type_id, true);
        if (source != null)
            {
            System.out.println(source.getDocumentationDescription());
            return;
            }

        System.out.println(String.format("type '%s' was not found in the project", type_id));
        }
    }


