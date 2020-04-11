package org.museautomation.core.task.input;

import org.museautomation.core.task.*;

/**
 * Can provide inputs to a task when asked.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TaskInputProvider
    {
    Object resolveInput(TaskInputResolutionResults resolved, TaskInput input);
    String getDescription();
    }