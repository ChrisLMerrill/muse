package org.museautomation.core.suite;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.task.*;

import java.util.*;

/**
 * A MuseTestSuite produced from a single test with varying sets of parameters.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("parameterized_test_suite")
@Deprecated
public class ParameterListTestSuite extends BaseMuseResource implements MuseTaskSuite
    {
    @Override
    public Iterator<TaskConfiguration> getTasks(MuseProject project)
	    {
	    return Collections.emptyIterator();
	    }

    @Override
    public Integer getTotalTaskCount(MuseProject project)
	    {
        return 0;
	    }

    @Override
    public ResourceType getType()
        {
        return new ParameterListTestSuiteSubtype();
        }

    public String getTestId()
        {
        return _testid;
        }

    @SuppressWarnings("unused")    // used by Jackson for de/serialization
    public void setTestId(String id)
        {
        _testid = id;
        }

    @SuppressWarnings("unused")    // used by Jackson for de/serialization
    public String getDataTableId()
        {
        return _datatable_id;
        }

    @SuppressWarnings("unused")    // used by Jackson for de/serialization
    public void setDataTableId(String id)
        {
        _datatable_id = id;
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
    private String _datatable_id;
    private String _testid;

    @SuppressWarnings("unused,WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class ParameterListTestSuiteSubtype extends ResourceSubtype
        {
        public ParameterListTestSuiteSubtype()
            {
            super(TYPE_ID, "Parameterized Test Suite", ParameterListTestSuite.class, new TaskSuiteResourceType());
            }

        @Override
        public boolean isInternalUseOnly()
            {
            return true;
            }
        }

    public final static String TYPE_ID = ParameterListTestSuite.class.getAnnotation(MuseTypeId.class).value();
    }


