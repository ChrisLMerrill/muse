package org.museautomation.core.step;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public enum StepExecutionStatus
    {
    /**
     * Step executed successfully.
     */
    COMPLETE,

    /**
     * Step executed successfully, but is not yet finished. It expects to be executed again.
     */
    INCOMPLETE,

    /**
     * Step executed successfully, but encountered an _expected_ failure condition
     */
    FAILURE,

    /**
     * Step completed successfully, and requests that the current execution context be exited.
     */
    @Deprecated
    RETURN,       // no longer used?

    /**
     * Step was not able to execute successfully, due to an _unexpected_ code or configuration error.
     */
    ERROR,

    /**
     * Step execution was interrupted by an outside actor (likely a human).
     */
    @Deprecated
    INTERRUPTED,   // no longer used?
    }
