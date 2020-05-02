package org.museautomation.builtins.plugins.input;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.task.input.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InputProviderPlugin implements MusePlugin, TaskInputProvider
    {
    public InputProviderPlugin(TaskInputProvider provider)
        {
        _provider = provider;
        }

    @Override
    public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
        {
        return true;
        }

    @Override
    public void initialize(MuseExecutionContext context)
        {

        }

    @Override
    public List<ResolvedTaskInput> resolveInputs(TaskInputResolutionResults resolved, UnresolvedTaskInputs inputs, MuseExecutionContext context)
        {
        return _provider.resolveInputs(resolved, inputs, context);
        }

    @Override
    public String getDescription()
        {
        return _provider.getDescription();
        }

    @Override
    public void shutdown()
        {

        }

    @Override
    public String getId()
        {
        return "input-provider-plugin";
        }

    private final TaskInputProvider _provider;
    }