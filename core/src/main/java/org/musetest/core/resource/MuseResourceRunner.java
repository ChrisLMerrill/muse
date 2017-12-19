package org.musetest.core.resource;

import org.musetest.core.*;

/**
 * Implement this to be available to the command-line 'run' command.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseResourceRunner
    {
    boolean canRun(MuseResource resource);
    boolean run(MuseProject project, MuseResource resource, boolean verbose, String output_path, String runner_id);
    }

