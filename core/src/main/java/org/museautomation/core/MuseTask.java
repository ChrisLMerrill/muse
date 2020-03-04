package org.museautomation.core;

import com.fasterxml.jackson.annotation.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.state.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * A Test is executed to return a result (pass or fail).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTask extends MuseResource
    {
    boolean execute(TaskExecutionContext context);

    @JsonIgnore
    String getDescription();

    Map<String, ValueSourceConfiguration> getDefaultVariables();
    void setDefaultVariables(Map<String, ValueSourceConfiguration> default_variables);
    void setDefaultVariable(String name, ValueSourceConfiguration source);

    TaskInputSet getInputSet();
    TaskOutputSet getOutputSet();
    TaskInputStates getInputStates();
    TaskOutputStates getOutputStates();

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    class TaskResourceType extends ResourceType
        {
        public TaskResourceType()
            {
            super("task", "Task", MuseTask.class);
            }

        @Override
        public MuseResource create()
            {
            SteppedTask test = new SteppedTask();
            test.setStep(ContainsStep.createStarterStep());
            return test;
            }
        }
    }