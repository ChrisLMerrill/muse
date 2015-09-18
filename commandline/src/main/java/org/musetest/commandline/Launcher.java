package org.musetest.commandline;

import io.airlift.airline.*;
import org.musetest.core.commandline.*;
import org.reflections.*;

import javax.imageio.spi.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Launcher
    {
    @SuppressWarnings("unchecked")
    public static void main(String[] args)
        {
        // dynamically lookup the commands using Java's ServiceRegistry. This looks at the META-INF/service files in jars on the classpath.
        Iterator<MuseCommand> commands = ServiceRegistry.lookupProviders(MuseCommand.class);
        List<Class<? extends Runnable>> implementors = new ArrayList<>();
        while (commands.hasNext())
            implementors.add((commands.next().getClass()));

        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("muse")
            .withDescription("Muse command-line tools")
            .withDefaultCommand(Help.class)
            .withCommands(Help.class)
            .withCommands(implementors);
        Cli<Runnable> muse_parser = builder.build();

        try
            {
            muse_parser.parse(args).run();
            }
        catch (Exception e)
            {
            muse_parser.parse(new String[0]).run();
            }
        }

    static
        {
        Reflections.log = null;
        }
    }


