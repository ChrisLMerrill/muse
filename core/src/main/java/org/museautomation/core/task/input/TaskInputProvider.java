package org.museautomation.core.task.input;

import java.util.*;

/**
 * Resolve inputs for a task. 
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TaskInputProvider
    {
    List<ResolvedTaskInput> resolveInputs(TaskInputResolutionResults resolved, UnresolvedTaskInputs inputs);
    String getDescription();
    }