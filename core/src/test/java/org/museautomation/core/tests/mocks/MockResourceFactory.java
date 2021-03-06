package org.museautomation.core.tests.mocks;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockResourceFactory implements MuseResourceFactory
    {
    @Override
    public List<MuseResource> createResources(ResourceOrigin origin, ClassLocator classes) throws IOException
        {
        ArrayList<MuseResource> resources = new ArrayList<>();
        if (origin instanceof MockResourceOrigin)
            resources.add(((MockResourceOrigin)origin).getResource());
        return resources;
        }
    }


