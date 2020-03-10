package org.museautomation.builtins.plugins.input;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.task.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InjectInputsPlugin extends GenericConfigurablePlugin
    {
    public InjectInputsPlugin(GenericResourceConfiguration configuration)
        {
        super(configuration);
        }

    @Override
    protected boolean applyToContextType(MuseExecutionContext context)
        {
        return false;
        }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseExecutionError
        {
        List<InputProvider> providers = Plugins.findAll(InputProvider.class, context);
        if (providers.isEmpty())
            {
            MessageEventType.raiseMessageAndThrowError(context, "No InputProvider plugins were found. InjectInputsPlugin is unable to inject inputs into the context. This is probably a configuration problem.");
            return;
            }

        MuseTask task = TaskExecutionContext.findTask(context);
//        InputProvider last_chance = null; TODO
        for (InputProvider provider : providers)
            {
            Map<String, Object> input_values = provider.gatherInputValues(task.getInputSet(), context);
            for (String name : input_values.keySet())
                {
                Object value = input_values.get(name);
                TaskInput input = task.getInputSet().getInput(name);
                if (input == null)
                    MessageEventType.raiseError(context, String.format("InjectInputsPlugin received input %s from an input provider, but it is not declared as a task input", name));
                else if (input.getType().isInstance(value))
                    context.setVariable(name, value);
                else
                    MessageEventType.raiseWarning(context, String.format("InjectInputsPlugin rejected a value of class '%s' because input '%s' expects type '%s'. The value is %s", value.getClass().getSimpleName(), name, input.getType().getName(), value));
                }
            }
        }
    }