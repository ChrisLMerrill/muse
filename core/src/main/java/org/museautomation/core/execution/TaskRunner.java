package org.museautomation.core.execution;

import org.museautomation.core.context.*;

/**
 * Runs the task. If this is a synchronous runner, it will block until the task completes (the task
 * _might_ be run on a separate thread, depending on the specfic implementation).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TaskRunner
    {
    TaskExecutionContext getExecutionContext();
    void runTask();

    /**
     * Returns true if the task completed normally, false if it did not. Null if the task has not yet completed.
     */
    Boolean completedNormally();
    }

