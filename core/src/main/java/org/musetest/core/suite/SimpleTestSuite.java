package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.test.*;

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

    @Override
    public Integer getTotalTestCount(MuseProject project)
	    {
	    return _tests.size();
	    }

    public void add(MuseTest test)
        {
        _tests.add(new BasicTestConfiguration(test.getId()));
        }

    @Override
    public ResourceType getType()
        {
        return new TestSuiteResourceType();
        }

    private List<TestConfiguration> _tests = new ArrayList<>();
    }