package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugins.*;
import org.musetest.core.util.*;

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
    public Iterator<TestConfiguration> getTests(MuseProject project)
	    {
	    MuseTest test = project.getResourceStorage().getResource(_testid, MuseTest.class);
	    if (test == null)
		    test = new MissingTest(_testid);

	    List<TestConfiguration> tests = new ArrayList<>();
	    List<Map<String, Object>> parameters;
	    if (_parameters != null)
		    parameters = _parameters;
	    else if (_datatable_id != null)
		    parameters = createParametersFromDataTable(project);
	    else
		    throw new IllegalStateException("ParameterListTestSuite requires either the DataTableId or Parameters properties.");
	    for (Map<String, Object> param_set : parameters)
		    {
		    TestConfiguration config = new TestConfiguration(test, new VariableMapInitializer(param_set));
		    config.setName(String.format("%s (%s)", test.getDescription(), Stringifiers.find(param_set).toString()));
		    tests.add(config);
		    }
	    return tests.iterator();
	    }

    private List<Map<String, Object>> createParametersFromDataTable(MuseProject project)
        {
        List<Map<String, Object>> param_list = new ArrayList<>();

        ResourceToken token = project.getResourceStorage().findResource(_datatable_id);
        if (token == null)
            throw new IllegalStateException("DataTable not found in the project: " + _datatable_id);
        if (!(token.getResource() instanceof DataTable))
            throw new IllegalStateException("ParameterListTestSuite requires the DataTableId corresponds to a DataTable resource in the project");

        DataTable table = (DataTable) token.getResource();
        String[] names = table.getColumnNames();
        for (int row = 0; row < table.getNumberRows(); row++)
            {
            Map<String, Object> params = new HashMap<>();
            for (int column = 0; column < names.length; column++)
                params.put(names[column], table.getData(column, row));
            param_list.add(params);
            }

        return param_list;
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
        String old_id = _testid;
        _testid = id;
        if (!Objects.equals(old_id, id))
            for (ChangeListener listener : _listeners)
                listener.testIdChanged(old_id, id);
        }

    @SuppressWarnings("unused")    // used by Jackson for de/serialization
    public String getDataTableId()
        {
        return _datatable_id;
        }

    @SuppressWarnings("unused")    // used by Jackson for de/serialization
    public void setDataTableId(String id)
        {
        String old_id = _datatable_id;
        _datatable_id = id;
        if (!Objects.equals(old_id, id))
            for (ChangeListener listener : _listeners)
                listener.datatableIdChanged(old_id, id);
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

    @SuppressWarnings("unused")  // public API
    public void addListener(ChangeListener listener)
        {
        _listeners.add(listener);
        }

    @SuppressWarnings("unused")  // public API
    public void removeListener(ChangeListener listener)
        {
        _listeners.remove(listener);
        }

    private List<Map<String, Object>> _parameters;
    private String _datatable_id;
    private String _testid;

    private transient Set<ChangeListener> _listeners = new HashSet<>();

    @SuppressWarnings("WeakerAccess")  // public API
    public interface ChangeListener
        {
        void testIdChanged(String old_id, String new_id);
        void datatableIdChanged(String old_id, String new_id);
        }

    @SuppressWarnings("unused,WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class ParameterListTestSuiteSubtype extends ResourceSubtype
        {
        public ParameterListTestSuiteSubtype()
            {
            super(TYPE_ID, "Parameterized Test Suite", ParameterListTestSuite.class, new MuseTestSuite.TestSuiteResourceType());
            }
        }

    public final static String TYPE_ID = ParameterListTestSuite.class.getAnnotation(MuseTypeId.class).value();
    }


