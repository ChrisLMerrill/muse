package org.musetest.core.resource;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ProjectResourceListener
    {
    void resourceAdded(ResourceToken added);
    void resourceRemoved(ResourceToken removed);
    }


