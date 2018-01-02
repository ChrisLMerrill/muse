package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleTestSuite extends BaseMuseResource implements MuseTestSuite
    {
    @Override
    public Iterator<TestConfiguration> getTests(MuseProject project)
	    {
	    return _tests.iterator();
	    }

    public void add(MuseTest test)
        {
        _tests.add(new TestConfiguration(test));
        }

    @Override
    public ResourceType getType()
        {
        return new TestSuiteResourceType();
        }

    private List<TestConfiguration> _tests = new ArrayList<>();
    }