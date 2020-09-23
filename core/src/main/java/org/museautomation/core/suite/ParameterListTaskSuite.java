package org.museautomation.core.suite;

import org.museautomation.builtins.plugins.init.*;
import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.task.*;
import org.slf4j.*;

import java.util.*;

/**
 * A MuseTaskSuite produced from a single task with varying sets of parameters.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("parameterized_task_suite")
public class ParameterListTaskSuite extends BaseMuseResource implements MuseTaskSuite
    {
    @SuppressWarnings("unused") // instantiated via reflection
    public ParameterListTaskSuite()
        {
        }

    public ParameterListTaskSuite(List<Map<String, Object>> parameters, String datatable_id, String taskid)
        {
        _parameters = parameters;
        _datatable_id = datatable_id;
        _taskid = taskid;
        }

    @Override
    public Iterator<TaskConfiguration> getTasks(MuseProject project)
	    {
	    MuseTask task = project.getResourceStorage().getResource(_taskid, MuseTask.class);
	    if (task == null)
		    task = new MissingTask(_taskid);

	    List<TaskConfiguration> tasks = new ArrayList<>();
	    List<Map<String, Object>> parameters = Collections.emptyList();
	    if (_parameters != null)
		    parameters = _parameters;
	    else if (_datatable_id != null)
		    parameters = createParametersFromDataTable(project);
	    else
        LOG.error("ParameterListTaskSuite requires either the DataTableId or Parameters properties.");
	    int repeat = 1;
	    for (Map<String, Object> param_set : parameters)
		    {
		    BasicTaskConfiguration config = new BasicTaskConfiguration(_taskid);
		    config.addPlugin(new VariableMapInitializer(param_set));
		    config.setName(String.format("%s/%s-%d", getId(), task.getDescription(), repeat));
		    tasks.add(config);
		    repeat++;
		    }
	    return tasks.iterator();
	    }

    @Override
    public Integer getTotalTaskCount(MuseProject project)
	    {
	    if (_datatable_id == null)
	        return 0;
	    if (_parameters == null)
            {
            DataTable table = getDataTable(project);
            if (table == null)
                return 0;
            return table.getNumberRows();
            }
        else
	    	return _parameters.size();
	    }

    private List<Map<String, Object>> createParametersFromDataTable(MuseProject project)
        {
        List<Map<String, Object>> param_list = new ArrayList<>();

        DataTable table = getDataTable(project);
        if (table == null)
            return param_list;
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

    private DataTable getDataTable(MuseProject project)
	    {
	    ResourceToken<? extends MuseResource> token = project.getResourceStorage().findResource(_datatable_id);
	    if (token == null)
            {
            LOG.error("DataTable not found in the project: " + _datatable_id);
            return null;
            }
        if (!(token.getResource() instanceof DataTable))
            {
            LOG.error("ParameterListTaskSuite requires the DataTableId corresponds to a DataTable resource in the project");
            return null;
            }

        return (DataTable) token.getResource();
	    }

    @Override
    public ResourceType getType()
        {
        return new ParameterListTaskSuiteSubtype();
        }

    @SuppressWarnings("unused")  // public API (for GUI)
    public String getTaskId()
        {
        return _taskid;
        }

    @SuppressWarnings("unused")    // used by Jackson for de/serialization
    public void setTaskId(String id)
        {
        String old_id = _taskid;
        _taskid = id;
        if (!Objects.equals(old_id, id))
            for (ChangeListener listener : _listeners)
                listener.taskIdChanged(old_id, id);
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
    private String _taskid;

    private final transient Set<ChangeListener> _listeners = new HashSet<>();

    public interface ChangeListener
        {
        void taskIdChanged(String old_id, String new_id);
        void datatableIdChanged(String old_id, String new_id);
        }

    @SuppressWarnings("unused,WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class ParameterListTaskSuiteSubtype extends ResourceSubtype
        {
        public ParameterListTaskSuiteSubtype()
            {
            super(TYPE_ID, "Parameterized Task Suite", ParameterListTaskSuite.class, new TaskSuiteResourceType());
            }
        }

    public final static String TYPE_ID = ParameterListTaskSuite.class.getAnnotation(MuseTypeId.class).value();

    final static Logger LOG = LoggerFactory.getLogger(ParameterListTaskSuite.class);
    }