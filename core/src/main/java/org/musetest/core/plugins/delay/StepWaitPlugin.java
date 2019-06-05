package org.musetest.core.plugins.delay;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.values.*;


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepWaitPlugin extends GenericConfigurableTestPlugin
    {
    public StepWaitPlugin(StepWaitPluginConfiguration configuration)
        {
        super(configuration);
        }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseExecutionError
        {
        Long delay_param = BaseValueSource.getValue(BaseValueSource.getValueSource(_configuration.parameters(), StepWaitPluginConfiguration.DELAY_TIME, false, context.getProject()), context, true, Long.class);
        if (delay_param != null)
            _delay_ms = delay_param;

        context.addEventListener(event ->
            {
            if (EndStepEventType.TYPE_ID.equals(event.getTypeId()))
                {
                try
                    {
                    Thread.sleep(_delay_ms);
                    }
                catch (InterruptedException e)
                    {
                    System.out.println("interrupted");
                    }
                }
            });
        }

    private long _delay_ms = StepWaitPluginConfiguration.DEFAULT_DELAY_TIME;
    }