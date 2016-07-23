package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * A MuseTestSuite produced from a single test with varying sets of parameters.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("parameterized_test_suite")
public class ParameterListTestSuite implements MuseTestSuite
    {
    @Override
    public List<TestConfiguration> generateTestList(MuseProject project)
        {
        List<TestConfiguration> tests = new ArrayList<>();
        for (Map<String, Object> param_set : _parameters)
            {
            TestExecutionContext context = new DefaultTestExecutionContext(project);
            for (String name : param_set.keySet())
                context.setVariable(name, param_set.get(name), VariableScope.Execution);
            MuseTest test = project.findResource(_testid, MuseTest.class);
            if (test == null)
                test = new MissingTest(_testid);
            tests.add(new TestConfiguration(test, context));
            }
        return tests;
        }

    @Override
    public ResourceMetadata getMetadata()
        {
        return _metadata;
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

    private ResourceMetadata _metadata = new ResourceMetadata();
    private List<Map<String, Object>> _parameters;
    private String _testid;
    }


