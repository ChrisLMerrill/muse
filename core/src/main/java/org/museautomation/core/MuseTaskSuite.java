package org.museautomation.core;

import org.museautomation.core.resource.types.*;
import org.museautomation.core.task.*;

import java.util.*;

/**
 * A test suite provides a list of MuseTests to be executed.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTaskSuite extends MuseResource
    {
    Iterator<TaskConfiguration> getTasks(MuseProject project);

    /**
     * Returns null if the test suite cannot calculate its total (e.g. it could execute infinitely).
     */
    Integer getTotalTaskCount(MuseProject project);

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    class TaskSuiteResourceType extends ResourceType
        {
        public TaskSuiteResourceType()
            {
            super("tasksuite", "Task Suite", MuseTaskSuite.class);
            }
        }
    }