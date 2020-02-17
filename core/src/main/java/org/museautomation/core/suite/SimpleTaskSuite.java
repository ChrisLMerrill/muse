package org.museautomation.core.suite;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.task.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleTaskSuite extends BaseMuseResource implements MuseTaskSuite
    {
    @Override
    public Iterator<TaskConfiguration> getTasks(MuseProject project)
	    {
	    return _tasks.iterator();
	    }

    @Override
    public Integer getTotalTaskCount(MuseProject project)
	    {
	    return _tasks.size();
	    }

    public void add(MuseTask test)
        {
        _tasks.add(new BasicTaskConfiguration(test.getId()));
        }

    @Override
    public ResourceType getType()
        {
        return new TaskSuiteResourceType();
        }

    private List<TaskConfiguration> _tasks = new ArrayList<>();
    }