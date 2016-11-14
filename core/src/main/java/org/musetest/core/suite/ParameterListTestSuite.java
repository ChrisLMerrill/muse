package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.context.initializers.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.test.*;

import java.util.*;

/**
 * A MuseTestSuite produced from a single test with varying sets of parameters.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("parameterized_test_suite")
public class ParameterListTestSuite extends BaseMuseResource implements MuseTestSuite
    {
    @Override
    public List<TestConfiguration> generateTestList(MuseProject project)
        {
        MuseTest test = project.getResourceStorage().getResource(_testid, MuseTest.class);
        if (test == null)
            test = new MissingTest(_testid);

        List<TestConfiguration> tests = new ArrayList<>();
        for (Map<String, Object> param_set : _parameters)
            tests.add(new TestConfiguration(test, new VariableMapInitializer(param_set)));
        return tests;
        }

    @Override
    public ResourceType getType()
        {
        return new MuseTest.TestResourceType();
        }

    public String getTestId()
        {
        return _testid;
        }

    @SuppressWarnings("unused")    // used by Jackson for de/serialization
    public void setTestId(String testid)
        {
        _testid = testid;
        }

    public List<Map<String, Object>> getParameters()
        {
        return _parameters;
        }

    @SuppressWarnings("unused")    // used by Jackson for de/serialization
    public void setParameters(List<Map<String, Object>> parameter_map_list)
        {
        _parameters = parameter_map_list;
        }

    private List<Map<String, Object>> _parameters;
    private String _testid;
    }


