package org.museautomation.core.commandline;

import io.airlift.airline.*;
import org.museautomation.core.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * Extension to the Muse command-line launcher to list available types of ?something?.
 *
 * This class is registered as a service in /META-INF/services/org.museautomation.core.commandline.MuseCommand.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Command(name = "list", description = "List available types (steps, sources)")
public class ListCommand extends MuseCommand
    {
    @Arguments(description = "The kind of thing to list (steps or sources)", required = true)
    public String kind;

    @Option(name = "-v", description = "Verbose output - list the full description of each type")
    public boolean verbose;

    @Override
    public void run()
        {
        if (!kind.equals("steps") && !kind.equals("sources"))
            {
            System.out.println("Please choose 'steps' or 'sources'");
            return;
            }

        MuseProject project = openProject();
        switch (kind)
            {
            case "steps":
                List<StepDescriptor> steps = new ArrayList<>();
                steps.addAll(project.getStepDescriptors().findAll());
                Collections.sort(steps, (step1, step2) -> step1.getName().compareTo(step2.getName()));
                for (StepDescriptor step : steps)
                    if (verbose)
                        {
                        System.out.println(SEPARATOR);
                        System.out.println(step.getDocumentationDescription());
                        }
                    else
                        System.out.println(String.format("%s (%s): %s", step.getName(), step.getType(), step.getShortDescription()));
                break;
            case "sources":
                List<ValueSourceDescriptor> sources = new ArrayList<>();
                sources.addAll(project.getValueSourceDescriptors().findAll());
                Collections.sort(sources, (source1, source2) -> source1.getName().compareTo(source2.getName()));
                for (ValueSourceDescriptor source : sources)
                    if (verbose)
                        {
                        System.out.println(SEPARATOR);
                        System.out.println(source.getDocumentationDescription());
                        }
                    else
                        System.out.println(String.format("%s (%s): %s", source.getName(), source.getType(), source.getShortDescription()));
                if (verbose)
                    System.out.println(SEPARATOR);
                break;
            }
        }

    final static String SEPARATOR = "---------------------------------------------------------------------------------------------";
    }


