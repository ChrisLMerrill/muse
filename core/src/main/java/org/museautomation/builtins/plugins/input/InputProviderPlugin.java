package org.museautomation.builtins.plugins.input;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.state.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InputProviderPlugin implements MusePlugin, InputProvider
    {
    public InputProviderPlugin(InputProvider provider)
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
    public boolean isLastChanceProvider()
        {
        return _provider.isLastChanceProvider();
        }

    @Override
    public Map<String, Object> gatherInputValues(TaskInputSet inputs, MuseExecutionContext context)
        {
        return _provider.gatherInputValues(inputs, context);
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

    private InputProvider _provider = null;
    private boolean _last_chance = false;
    }