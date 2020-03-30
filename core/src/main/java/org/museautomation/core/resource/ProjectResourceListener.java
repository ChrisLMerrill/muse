package org.museautomation.core.resource;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ProjectResourceListener
    {
    void resourceAdded(ResourceToken<MuseResource> added);
    void resourceRemoved(ResourceToken<MuseResource> removed);
    }


