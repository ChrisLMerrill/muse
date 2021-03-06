package org.museautomation.core.task;

/**
 * A test resource that should be shut down after a test.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface Shuttable
    {
    void shutdown();
    }

