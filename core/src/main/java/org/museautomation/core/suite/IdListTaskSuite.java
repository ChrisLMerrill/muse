package org.museautomation.core.suite;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.task.*;
import org.slf4j.*;

import java.util.*;

/**
 * A MuseTaskSuite that creates tasks based on a list of task IDs and/or task suite ids.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("suite_of_ids")
public class IdListTaskSuite extends BaseMuseResource implements MuseTaskSuite
    {
    @Override
    public Iterator<TaskConfiguration> getTasks(MuseProject project)
	    {
	    List<TaskConfiguration> direct_tasks = new ArrayList<>(_task_ids.size());
	    List<Iterator<TaskConfiguration>> suite_iterators = new ArrayList<>();
	    for (String id : _task_ids)
		    {
		    MuseResource resource = project.getResourceStorage().getResource(id);
		    if (resource instanceof MuseTask)
			    direct_tasks.add(new BasicTaskConfiguration(id));
		    else if (resource instanceof MuseTaskSuite)
			    suite_iterators.add(((MuseTaskSuite) resource).getTasks(project));
		    else
			    {
			    LOG.error("Task with id {} was not found in the project", id);
			    direct_tasks.add(new BasicTaskConfiguration(id));
			    }
		    }

	    CompoundTaskConfigurationIterator all = new CompoundTaskConfigurationIterator();
	    all.add(direct_tasks.iterator());
	    for (Iterator<TaskConfiguration> iterator : suite_iterators)
	    	all.add(iterator);

	    return all;
	    }

    @Override
    public Integer getTotalTaskCount(MuseProject project)
	    {
	    Integer total = 0;
	    boolean possibly_infinite = false;

	    for (String id : _task_ids)
		    {
		    MuseResource resource = project.getResourceStorage().getResource(id);
		    if (resource instanceof MuseTask)
			    total++;
		    else if (resource instanceof MuseTaskSuite)
			    {
			    Integer suite_count = ((MuseTaskSuite)resource).getTotalTaskCount(project);
			    if (suite_count == null)
			    	possibly_infinite = true;
			    else
			    	total += suite_count;
			    }
		    }

	    if (total == 0 && possibly_infinite)
	    	return null;
	    return total;
	    }

    public List<String> getTaskIds()
        {
        return Collections.unmodifiableList(_task_ids);
        }

    @SuppressWarnings("unused")  // used by Jackson
    public void setTaskIds(List<String> ids)
        {
        _task_ids = ids;
        }

    // kept for backwards-compatability with files created before the Test -> Task migration
    @SuppressWarnings("unused")  // used by Jackson
    @Deprecated
    public void setTestIds(List<String> ids)
        {
        _task_ids = ids;
        }

    // kept for backwards-compatability with files created before the Test -> Task migration
    @SuppressWarnings("unused")  // used by Jackson
    @Deprecated
    public List<String> getTestIds()
        {
        return null;
        }

    @SuppressWarnings({"unused", "UnusedReturnValue"})  // public API
    public boolean addTaskId(String id)
        {
        boolean added = _task_ids.add(id);
        for (ChangeListener listener : _listeners)
            listener.taskIdAdded(id);
        return added;
        }

    @SuppressWarnings("unused")  // public API
    public boolean removeTaskId(String id)
        {
        boolean added = _task_ids.remove(id);
        for (ChangeListener listener : _listeners)
            listener.taskIdRemoved(id);
        return added;
        }

    @Override
    public ResourceType getType()
        {
        return new IdListTaskSuiteSubtype();
        }

    public void addChangeListener(ChangeListener listener)
        {
        _listeners.add(listener);
        }

    public boolean removeChangeListener(ChangeListener listener)
        {
        return _listeners.remove(listener);
        }

    @SuppressWarnings("unused")  // public API
    public interface ChangeListener
        {
        void taskIdAdded(String id);
        void taskIdRemoved(String id);
        }

    @SuppressWarnings("unused,WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class IdListTaskSuiteSubtype extends ResourceSubtype
        {
        public IdListTaskSuiteSubtype()
            {
            super(TYPE_ID, "List of tasks suite", IdListTaskSuite.class, new TaskSuiteResourceType());
            }
        }

    private List<String> _task_ids = new ArrayList<>();

    private transient Set<ChangeListener> _listeners = new HashSet<>();

    public final static String TYPE_ID = IdListTaskSuite.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(IdListTaskSuite.class);
    }