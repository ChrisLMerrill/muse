package org.musetest.core.commandline;

import io.airlift.airline.*;
import org.musetest.core.*;
import org.musetest.core.util.*;

/**
 * Extension to the Muse command-line launcher to describe type of step or value source
 *
 * This class is registered as a service in /META-INF/services/org.musetest.core.commandline.MuseCommand.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Command(name = "describe", description = "Run a project resource (test, test suite, ...)")
public class DescribeCommand extends MuseCommand
    {
    @Arguments(description = "The id of the thing to describe (type id of step or source)", required = true)
    public String type_id;

    @Override
    public void run()
        {
        MuseProject project = openProject();

        Class class_for_type = new TypeLocator(project).getClassForTypeId(type_id);
        if (class_for_type == null)
            {
            System.out.println(String.format("type %s is not recognized in the project", type_id));
            return;
            }

        if (MuseStep.class.isAssignableFrom(class_for_type))
            System.out.println(project.getStepDescriptors().get(type_id).getDocumentationDescription());
        else if (MuseValueSource.class.isAssignableFrom(class_for_type))
            System.out.println(project.getValueSourceDescriptors().get(type_id).getDocumentationDescription());
        }
    }


