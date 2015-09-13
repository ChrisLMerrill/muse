package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.resource.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleTestSuite implements MuseTestSuite
    {
    @Override
    public List<TestConfiguration> generateTestList(MuseProject project)
        {
        return _tests;
        }

    public void add(MuseTest test)
        {
        _tests.add(new TestConfiguration(test));
        }

    @Override
    public ResourceMetadata getMetadata()
        {
        return _metadata;
        }

    private ResourceMetadata _metadata = new ResourceMetadata();
    private List<TestConfiguration> _tests = new ArrayList<>();
    }


