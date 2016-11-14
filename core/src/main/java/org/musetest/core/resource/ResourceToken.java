package org.musetest.core.resource;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ResourceToken<T extends MuseResource> extends ResourceInfo
    {
    T getResource();
    }


