package org.museautomation.core.task.input;

import org.museautomation.core.*;
import org.museautomation.core.task.*;

import java.util.*;

/**
 * Resolve inputs for a task. 
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TaskInputProvider
    {
    List<ResolvedTaskInput> resolveInputs(TaskInputResolutionResults resolved, TaskInputSet inputs, MuseExecutionContext context);
    String getDescription();
    }