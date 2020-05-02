package org.museautomation.core.task.input;

import org.museautomation.core.*;
import org.museautomation.core.task.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskInputResolutionResults
    {
    public ResolvedTaskInput getResolvedInput(String name)
        {
        for (ResolvedTaskInput input : _resolved_inputs)
            if (input.getName().equals(name))
                return input;
        return null;
        }

    public void addResolvedInput(ResolvedTaskInput input)
        {
        ResolvedTaskInput existing = getResolvedInput(input.getName());
        if (existing != null)
            _resolved_inputs.remove(existing);
        _resolved_inputs.add(input);
        }

    public boolean inputsSatisfied(MuseTask task)
        {
        return getUnsatisfiedInputs(task).isEmpty();
        }

    public List<TaskInput> getUnsatisfiedInputs(MuseTask task)
        {
        List<TaskInput> list = new ArrayList<>();
        for (TaskInput input : task.getInputSet().getInputs())
            {
            ResolvedTaskInput resolved = getResolvedInput(input.getName());
            if (resolved == null)
                {
                if (input.isRequired())
                    list.add(input);
                }
            else
                {
                if (!input.getType().isInstance(resolved.getValue()))
                    list.add(input);
                }
            }
        return list;
        }

    public String
    getUnsatisfiedInputDescription(MuseTask task)
        {
        List<TaskInput> inputs = getUnsatisfiedInputs(task);
        if (inputs.isEmpty())
            return "all inputs satisfied";
        StringBuilder description = new StringBuilder();
        for (TaskInput input : inputs)
            {
            if (description.length() > 0)
                description.append(",");
            description.append(input.getName());
            }
        return description.toString();
        }

    public List<ResolvedTaskInput> getResolvedInputs()
        {
        return Collections.unmodifiableList(_resolved_inputs);
        }

    public UnresovledTaskInputs getUnresolvedInputs(TaskInputSet all_inputs)
        {
        List<TaskInput> unresolved = new ArrayList<>();
        for (TaskInput input : all_inputs.getInputs())
            if (getResolvedInput(input.getName()) == null)
                unresolved.add(input);
        return new UnresovledTaskInputs(unresolved);
        }

    private final List<ResolvedTaskInput> _resolved_inputs = new ArrayList<>();
    }