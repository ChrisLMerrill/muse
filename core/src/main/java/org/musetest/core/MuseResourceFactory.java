package org.musetest.core;

import org.musetest.core.resource.*;
import org.musetest.core.util.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseResourceFactory extends MuseDynamicLoadable
    {
    /**
     * Create MuseResources from the origin provided.
     * Implementers should not propogate exceptions encountered from unexpected data.
     *
     * @return A list of MuseResources or an empty list.
     * @throws IOException
     */
    List<MuseResource> createResources(ResourceOrigin origin, ClassLocator classes) throws IOException;
    }


